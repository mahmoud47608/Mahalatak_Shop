# sync-forever.ps1
# Continuous agent runner with logging + status

$logFile = ".\sync-log.txt"
$iteration = 1

function Write-Log {
    param([string]$message)

    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    $logMessage = "[$timestamp] $message"

    Write-Host $logMessage
    Add-Content -Path $logFile -Value $logMessage
}

Write-Log "===== Agent Loop Started ====="

while ($true) {

    Write-Log "Iteration $iteration - Running Claude..."

    try {
        $prompt = Get-Content .\agent\prompt.md -Raw

        $result = $prompt | claude -p --dangerously-skip-permissions 2>&1

        if ($LASTEXITCODE -eq 0) {
            Write-Log "Iteration $iteration - SUCCESS"
        }
        else {
            Write-Log "Iteration $iteration - FAILED (Exit Code: $LASTEXITCODE)"
        }

        # Optional: log response (comment لو كبير)
        # Add-Content -Path $logFile -Value $result

    }
    catch {
        Write-Log "Iteration $iteration - ERROR: $_"
    }

    Write-Log "Iteration $iteration - Sleeping 2 seconds..."
    Start-Sleep -Seconds 2

    $iteration++
}
