set TMP_PATH=%PATH%;
set PATH=D:\Programmi\apache-ant-1.8.4\bin;C:\Program Files\Java\jdk1.5.0_22\bin
SET JAVA_HOME=C:\Program Files\Java\jdk1.8.0_74

set ANT_HOME=D:\Programmi\apache-ant-1.8.4
set JAVA_HOME_CLIENT=C:\Program Files\Java\jdk1.8.0_74
set JAVA_HOME_SERVER=C:\Program Files\Java\jdk1.8.0_74
set PATH_OLD=%PATH%


@rem call "C:\Programmi\TortoiseSVN\bin\TortoiseProc.exe" /command:update /path:"." /closeonend:1

@rem TEST
 @rem call ant package-ear -Dtarget=tst-rp-01 -Djsp.precompile=true

 @rem call ant load-dependencies  -Dtarget=tst-rp-01

@rem java -version
@rem SVILUPPO

@rem call ant load-dependencies -Dtarget=tst-rp-01
call ant package-ear -Dtarget=tst-bas -Djsp.precompile=true
set PATH=%TMP_PATH%

pause

:exit