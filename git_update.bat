@echo off
echo ==========================================
echo    Actualizando repositorio CRSurfGuide
echo ==========================================

git status
echo.
set /p commit_msg="Escribe el mensaje del commit: "

git add .
git commit -m "%commit_msg%"
git push origin main

echo.
echo ==========================================
echo    ¡Proceso completado con exito!
echo ==========================================
pause