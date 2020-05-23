# BadenAir Flight Management-System

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

### H2 In-memory Datenbank

Starten mit Profil `h2`:  
* IntelliJ: `Edit Configurations...` -> Neue Spring Boot Konfiguration -> Main Klasse auswählen -> Unter `Sprng Boot` Sektion bei `Active Profiles` `h2` eintragen
* Maven: `mvn spring-boot:run '-Dspring-boot.run.profiles=h2'`

Zugriff auf die Konsole auf [localhost:8081/h2-console](localhost:8081/h2-console). JDBC URL bei dem Anmelden mit `jdbc:h2:mem:testdb` ersetzen.

### Keycloak API Zugriff

1. Datei `application-keycloak.template` duplizieren und Duplikat in `application-keycloak.yml` umbenennen
2. Username und Passwort des Keycloak Admins eintragen
3. Beim Start des Servers: `keycloak` als Profil übergeben (Syntax analog wie bei Profil `h2`)
