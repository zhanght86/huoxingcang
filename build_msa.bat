@set TESTSERVERDIR=\\192.168.1.144\share\»ðÐÇ²Õweb\ROOT.war
@set TESTUSER=nas
@set TESTPWD=4321
@set TESTDRIVE=z:
@set PCPATH=.

rem @set PCPATH=D:\MarsMSA\OpenIndiana\WEB_Marstor\msa

call build_marstor.bat
call build_ubackup.bat
call build_toyou.bat
call build_topsec.bat
call build_dhc.bat
copy  /Y "changelog.txt"  "dist\vendor\"

pause


