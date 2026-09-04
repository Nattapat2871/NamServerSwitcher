# Author: nattapat2871 (https://nattapat2871.me)
param()
$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.IO.Compression.FileSystem
$root = Split-Path -Parent $PSScriptRoot
$targets = Get-Content -Raw (Join-Path $root 'versions.json') | ConvertFrom-Json -AsHashtable
$results = Get-Content -Raw (Join-Path $root 'build/build-results.json') | ConvertFrom-Json
$modVersion = ((Get-Content (Join-Path $root 'gradle.properties') | Where-Object { $_ -match '^mod_version=' }) -split '=', 2)[1]
function Read-ZipText($zip, $name) {
    $entry = $zip.GetEntry($name)
    if (!$entry) { throw "Missing jar entry: $name" }
    $reader = [IO.StreamReader]::new($entry.Open())
    try { return $reader.ReadToEnd() } finally { $reader.Dispose() }
}
$manifest = foreach ($mc in @($targets.Keys | Sort-Object { [version]$_ })) {
    $result = @($results | Where-Object minecraft -eq $mc)
    if ($result.Count -ne 1 -or $result[0].exitCode -ne 0) { throw "No successful current build: $mc" }
    $filename = "namserverswitcher-$mc-$modVersion.jar"
    $path = Join-Path $root "dist/$filename"
    $zip = [IO.Compression.ZipFile]::OpenRead($path)
    try {
        $metadata = Read-ZipText $zip 'fabric.mod.json' | ConvertFrom-Json
        if ($metadata.id -ne 'serverswitcher' -or $metadata.version -ne $modVersion -or $metadata.depends.minecraft -ne $mc) { throw "Incorrect mod metadata: $mc" }
        if ($metadata.environment -ne 'client' -or $metadata.license -ne 'MIT') { throw "Incorrect environment/license: $mc" }
        if (!$zip.GetEntry($metadata.icon)) { throw "Missing icon: $mc" }
        if (!@($zip.Entries | Where-Object FullName -like 'LICENSE*').Count) { throw "Missing license: $mc" }
        if (@($zip.Entries | Where-Object FullName -like 'net/minecraft/*').Count) { throw "Game code included: $mc" }
        $namespace = if ($mc.StartsWith('26.')) { 'official' } else { 'intermediary' }
        $widener = Read-ZipText $zip $metadata.accessWidener
        $widenerHeader = (($widener -split '\r?\n')[0] -split '\s+') -join ' '
        if ($widenerHeader -ne "accessWidener v2 $namespace") { throw "Incorrect access widener namespace: $mc" }
        $classCount = 0
        foreach ($entry in @($zip.Entries | Where-Object FullName -like '*.class')) {
            $stream = $entry.Open()
            try {
                $header = [byte[]]::new(8)
                $stream.ReadExactly($header)
                $major = 256 * [int]$header[6] + [int]$header[7]
                if ($major -ne (44 + $targets[$mc].java)) { throw "Incorrect class version $major in $($entry.FullName) for $mc" }
                $classCount++
            } finally { $stream.Dispose() }
        }
        if (!$classCount) { throw "Empty mod: $mc" }
        $tests = 0
        foreach ($report in Get-ChildItem (Join-Path $root "build/$mc/test-results/test") -Filter 'TEST-*.xml') {
            [xml]$xml = Get-Content -Raw $report.FullName
            if ([int]$xml.testsuite.failures -ne 0 -or [int]$xml.testsuite.errors -ne 0) { throw "Failing tests: $mc" }
            $tests += [int]$xml.testsuite.tests
        }
        if ($tests -lt 9) { throw "Missing scroll-model tests: $mc" }
        [ordered]@{
            minecraft = $mc; version_number = "$modVersion+$mc"; file = $filename
            loaders = @('fabric'); java = $targets[$mc].java; namespace = $namespace
            sha256 = (Get-FileHash $path -Algorithm SHA256).Hash.ToLowerInvariant()
            sha512 = (Get-FileHash $path -Algorithm SHA512).Hash.ToLowerInvariant()
            unit_tests = $tests; in_game_tested = $false
            dependencies = @(
                @{ project_id = 'P7dR8mSH'; dependency_type = 'required' },
                @{ project_id = '9s6osm5g'; dependency_type = 'required' },
                @{ project_id = 'mOgUt4GM'; dependency_type = 'optional' }
            )
        }
    } finally { $zip.Dispose() }
}
$bundle = Join-Path $root 'dist/modrinth'
New-Item -ItemType Directory -Path $bundle -Force | Out-Null
$manifest | ConvertTo-Json -Depth 8 | Set-Content (Join-Path $bundle 'versions.json') -Encoding utf8
Copy-Item (Join-Path $root 'docs/MODRINTH*.md') -Destination $bundle -Force
Copy-Item (Join-Path $root 'docs/assets/quick-join.png'), (Join-Path $root 'docs/assets/pause-menu.png') -Destination $bundle -Force
foreach ($entry in $manifest) { Copy-Item (Join-Path $root "dist/$($entry.file)") -Destination $bundle -Force }
Copy-Item (Join-Path $root 'LICENSE') -Destination $bundle -Force
Compress-Archive -Path "$bundle/*" -DestinationPath (Join-Path $root "dist/namserverswitcher-$modVersion-modrinth.zip") -Force
$manifest | ForEach-Object { [pscustomobject]@{ Minecraft = $_.minecraft; Tests = $_.unit_tests; Namespace = $_.namespace; File = $_.file } } | Format-Table
Write-Host "Verified $($manifest.Count) artifacts; multiplayer gameplay is not tested."
