@echo off
REM Avvio dell'installazione
echo "Installazione avviata"

REM Creazione del volume Docker 'VolumeT9'
docker volume create VolumeT9 || echo "Errore nella creazione del volume"

REM Creazione del volume Docker 'VolumeT8'
docker volume create VolumeT8 || echo "Errore nella creazione del volume"

REM Creazione della rete Docker 'global-network'
docker network create global-network || echo "Errore nella creazione della rete"

REM Definizione dei percorsi delle directory da visitare
set list=  "./T1-G11/applicazione/manvsclass" "./T23-G1" "./T4-G18" "./T5-G2/t5" "./T6-G12/T6" "./T7-G31/RemoteCCC"  "./T8-G21\Progetto_SAD_GRUPPO21_TASK8\Progetto_def\opt_livelli\Prototipo2.0" "./T9-G19\Progetto-SAD-G19-master" "./api_gateway" "./ui_gateway"

REM Ciclo attraverso le directory specificate
(for %%a in (%list%) do (
   pushd .
   cd %%a
   echo "Installazione in corso in %%a"
   
   REM Avvio dei container Docker e gestione degli errori
   docker compose up -d --build || echo "Errore nell'installazione del Task %%a"
   
   popd 
))
REM Esecuzione del file entrypoint.sh all'interno del container remoteccc-app-1
echo "Esecuzione del file entrypoint.sh all'interno del container remoteccc-app-1"
docker exec -it remoteccc-app-1 /bin/bash -c "./entrypoint.sh"

REM Esecuzione del file installazione.sh all'interno del container manvsclass-controller-1
echo "Esecuzione del file installazione.sh all'interno del container manvsclass-controller-1"
docker exec -it manvsclass-controller-1 /bin/bash -c "./../VolumeT8/app/installazione.sh"

REM Messaggio di completamento dell'installazione
echo "Installazione terminata"
pause