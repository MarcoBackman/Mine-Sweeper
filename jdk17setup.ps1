$url = "https://download.oracle.com/java/17/archive/jdk-17.0.10_windows-x64_bin.exe"
$outputDirectory = Join-Path -Path $env:HOMEDRIVE -ChildPath $env:HOMEPATH
$outputDirectory = Join-Path -Path $outputDirectory -ChildPath "Downloads"
$outputFileName = Split-Path -Path $url -Leaf
$filePath = Join-Path -Path $outputDirectory -ChildPath $outputFileName


# Ensure the output directory exists
if (!(Test-Path $outputDirectory)) {
  New-Item -ItemType Directory -Force -Path $outputDirectory
}

if (Test-Path -Path $filePath) {
    Write-Output "File exists"
} else {
    Write-Output "File does not exist. Download in progress"
    Invoke-WebRequest -Uri $url -OutFile $filePath
}

Start-Process -FilePath $filePath -Wait

Read-Host -Prompt "Press Enter to continue"