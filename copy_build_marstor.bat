@set TESTSERVERDIR=\\192.168.1.144\share\»ðÐÇ²Õweb\ROOT.war
@set TESTUSER=nas
@set TESTPWD=4321
@set TESTDRIVE=z:
@set PCPATH=.

rem @set PCPATH=D:\MarsMSA\OpenIndiana\WEB_Marstor\msa

call build_marstor.bat
rem call build_ubackup.bat
rem call build_toyou.bat
rem call build_topsec.bat
rem call build_dhc.bat
rem copy  /Y "changelog.txt"  "dist\vendor\marstor\"

c:\windows\system32\net.exe use %TESTDRIVE% %TESTSERVERDIR% %TESTPWD% /User:%TESTUSER% 

for /f "tokens=1,2,3 delims=/ " %%a in ('date /t') do  @set CURRENTDATE=%%a-%%b-%%c
for /f "tokens=1,2 delims=: " %%x in ('time /t') do  @set CURRENTTIME=%%x-%%y

rem rd /S /Q "%TESTDRIVE%\"

if not exist %TESTDRIVE%\%CURRENTDATE%_%CURRENTTIME% mkdir %TESTDRIVE%\%CURRENTDATE%_%CURRENTTIME%_develop_db
xcopy  /Y /s /e "%PCPATH%\dist\vendor\marstor\*"  "%TESTDRIVE%\%CURRENTDATE%_%CURRENTTIME%_develop_db\" 

c:\windows\system32\net.exe use %TESTDRIVE% /DELETE

pause


