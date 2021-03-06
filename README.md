# BadenAir Flight Management-System

This was a demo project developed at the university of applied sciences in Offenburg.

## Should I use this?

No. Definitely no. This project is archived for a reason... This software may still contain various bugs and it's highly customized to some needs you probably don't have or want.

## Authors

* Niklas Studer
* Hannes Braun
* Jan Stürzel
* Jonas Kienzle
* Julia Merettig
* Friedrich Schmidt
* Janes Heuberger
* Denis Block

## Local development setup

### Setup Keycloak

1. Download Keycloak: [ZIP](https://downloads.jboss.org/keycloak/9.0.2/keycloak-9.0.2.zip), [TAR.GZ](https://downloads.jboss.org/keycloak/9.0.2/keycloak-9.0.2.tar.gz)
2. Entpacken und in den `bin/` Ordner gehen
3. Run keycloak:
    * Windows: `.\standalone.bat -b 0.0.0.0`
    * Linux/Mac: `./standalone.sh -b 0.0.0.0`
4. Bei [localhost:8080](localhost:8080) Username und Passwort vergeben
5. Keycloak config importieren (`kcadm.bat`/`kcadm.sh` befinden sich im `bin/` Ordner von Keycloak):
    * Admin CLI anmelden: `.\kcadm.bat config credentials --server http://localhost:8080/auth --realm master --user <USERNAME> --password <PASSWORD>`
    * Realm importieren (keycloak_config.json im Projektordner): `.\kcadm.bat create realms -f "<PATH-TO-CONFIG>"`
6. In der Keycloak-Admin-Konsole im Master-Realm unter Realm-Settings -> Tokens die "Access Token Lifespan" auf 30 Minuten erhöhen (und speichern).

### H2 In-memory Datenbank

Starten mit Profil `h2`:  
* IntelliJ: `Edit Configurations...` -> Neue Spring Boot Konfiguration -> Main Klasse auswählen -> Unter `Sprng Boot` Sektion bei `Active Profiles` `h2` eintragen
* Maven: `mvn spring-boot:run '-Dspring-boot.run.profiles=h2'`

Zugriff auf die Konsole auf [localhost:8081/h2-console](localhost:8081/h2-console). JDBC URL bei dem Anmelden mit `jdbc:h2:mem:testdb` ersetzen.

### Keycloak API Zugriff

1. Datei `application-keycloak.template` duplizieren und Duplikat in `application-keycloak.yml` umbenennen
2. Username und Passwort des Keycloak Admins eintragen
3. Beim Start des Servers: `keycloak` als Profil übergeben (Syntax analog wie bei Profil `h2`)
