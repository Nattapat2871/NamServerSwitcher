# Author: nattapat2871 (https://nattapat2871.me)
param([string[]]$Versions = @())
$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot
$targets = Get-Content -Raw (Join-Path $projectRoot 'versions.json') | ConvertFrom-Json -AsHashtable
if ($Versions.Count -eq 0) { $Versions = @($targets.Keys | Sort-Object { [version]$_ }) }
foreach ($version in $Versions) {
    if (!$targets.Contains($version)) { throw "Unsupported Minecraft version: $version" }
}
Push-Location $projectRoot
try {
    New-Item -ItemType Directory -Path 'build/logs' -Force | Out-Null
    $results = foreach ($version in $Versions) {
        Write-Host "Building Minecraft $version"
        & ./gradlew.bat collectRelease "-Pmc=$version" --console=plain --stacktrace *> "build/logs/$version.log"
        $result = [pscustomobject]@{ minecraft = $version; exitCode = $LASTEXITCODE }
        Write-Host "Minecraft $version finished with exit code $($result.exitCode)"
        $result
    }
    $results | ConvertTo-Json | Set-Content 'build/build-results.json' -Encoding utf8
    $results | Format-Table
    if (@($results | Where-Object exitCode -ne 0).Count -gt 0) { exit 1 }
} finally {
    Pop-Location
}
