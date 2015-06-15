@echo off
echo %* > latestChanges.txt
echo %* >> changelog.txt
git commit -am "%*"
