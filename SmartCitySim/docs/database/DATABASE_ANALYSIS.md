# DATABASE ANALYSIS
## Descrizione entità ambigue
#
## parking_areas
Entità che raccoglie l'insieme di *parking spots*.
## parking_spots
Entità che modella il singolo posteggio del veicolo.

#
## Decisioni prese
#
## Creazione tabella parking_areas
E' stato scelto di creare una tabella **parking_areas**, per avere a disposizione la latitudine e la longitudine del singolo parcheggio.

## Creazione tabella sensors_maintenance
Sono stati estrapolati alcuni dati dalla tabella **sensors** e messi nella tabella **sensors_maintenance**, per separare i dati relativi alla manutenzione da quelli descrittivi e funzionali dei sensori.

## Associazione ManyToMany tra parking_spots e sensors
Un sensore di tipo parking può essere associato a un solo parking spot mentre un sensore ambientale può essere associato a più parking spot (se ad esempio è nel raggio di 5 parking spot, verrà associato a tutti e 5).

Un parking spot può avere più sensori (sensore di parcheggio, di temperatura, di umidità ecc..)

## Creazione tabella paking_sensors
Questa tabella non era presente prima. Il campo *value* e *last_update* erano presenti nella tabella **parking_spots**, il che è stato ritenuto errato in quanto nella realtà modellata questi 2 elementi vengono gestiti da un sensore e non da un parking spot. Inoltre trattare un parking spot come un sensore di parcheggio porta ad avere un database poco efficente e quindi è stata creata un'altra tabella chiamata **paking_sensors**, per memorizzare le misurazioni dei sensori parking, dove per coerenza con le altre tipologie di misurazioni di sensori, il campo *last_update* è stato rinominato *timestamp*. 

Inoltre se in futuro si vuole salvare lo storico anche delle misurazioni dei sensori parking è molto più facile farlo con questa struttura.

## Tabella ManyToMany parking_spots_sensors
Questa tabella è creata in modo automatico nella maggior parte dei framework ORM, definendo la relazione ManyToMany tra **parking_spots** e **sensors**.

## Storico tabelle ambientali
Le tabelle ambientali mantengono lo storico delle misurazioni effettuate.

## Relazione OneToOne tra sensors e parking_sensors
Per i sensori di tipo parking non si vuole salvare uno storico come nei sensori ambientali, ma mantendere la misurazione del sensore in tempo reale, quindi ogni sensore ha un solo record associato nella tabella **parking_sensors**, che viene aggiornato in tempo reale alla nuova misurazione del sensore (si tiene quindi solo l'ultima misurazione inserita, sovrascrivendo la precedente).

## Tolto vincoli di unicità misurazioni ambientali
Sono stati tolti i vincoli di unicità che erano presenti per i campi (*fk_sensor_id*) e (*latitude*, *longitude*) nel codice SQL per creare le tabelle delle misurazioni ambientali, probabilmente inseriti per errore, dato che non permettono di inserire più di una misurazione per uno stesso sensore.

## Modificato nome tabella sensors_maintainer in sensors_maintenance
E' stato scelto un nome più appropriato per la tabella relativa ai dati di manutenzione dei sensori.