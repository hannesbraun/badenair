Vagrant.configure("2") do |config|
  config.vm.box = "debian/buster64"
  config.vm.box_version = "10.3.0"

  config.vm.provider :virtualbox do |vb|
    vb.name = "BadenAir Server"
    vb.memory = 2048
    vb.cpus = 2
  end

  config.vm.network "forwarded_port", guest: 31416, host: 31416, protocol: "tcp"
  config.vm.network "forwarded_port", guest: 42069, host: 42069, protocol: "tcp"
  config.vm.network "forwarded_port", guest: 3306, host: 3306
  config.vm.network "forwarded_port", guest: 8080, host: 8080

  config.vm.synced_folder ".", "/vagrant", type: "rsync",
      rsync__exclude: [".git/", "frontend-customers/node_modules/", "frontend-employees/node_modules/"]

  config.vm.provision "shell", inline: <<-SHELL
         sudo apt-get update -y

         # Install AdoptOpenJDK and other useful stuff
         sudo apt-get install -y curl gnupg software-properties-common psmisc htop unzip
         wget -qO - https://adoptopenjdk.jfrog.io/adoptopenjdk/api/gpg/key/public | sudo apt-key add -
         sudo add-apt-repository --yes https://adoptopenjdk.jfrog.io/adoptopenjdk/deb/
         sudo apt-get update -y
         sudo apt-get install -y adoptopenjdk-11-openj9
         
         # Install Node.js
         sudo curl -sL https://deb.nodesource.com/setup_12.x | bash -
         sudo apt-get install -y nodejs

         # Install Angular
         sudo npm install -g @angular/cli

         cd /vagrant/frontend-customers/
         npm install
         cd /vagrant/frontend-employees/
         npm install

         # Install and configure Database
         sudo apt-get install -y mariadb-server
         sudo /etc/init.d/mysql restart
         mysql -e "CREATE DATABASE badenair;"
         mysql -e "CREATE USER 'badenair_user' IDENTIFIED BY 'badenair_password;'"
         mysql -e "GRANT USAGE ON *.* TO 'badenair_user'@'%' IDENTIFIED BY 'badenair_password';"
         mysql -e "GRANT ALL privileges ON badenair.* TO 'badenair_user'@localhost IDENTIFIED BY 'badenair_password';"
         mysql -e "FLUSH PRIVILEGES;"
         sudo /etc/init.d/mysql restart

         # Install and configure keycloak
         wget -q https://downloads.jboss.org/keycloak/9.0.2/keycloak-9.0.2.zip
         unzip keycloak-9.0.2.zip && rm keycloak-9.0.2.zip
         cp -r keycloak-9.0.2 /home/vagrant/keycloak
         chown -R vagrant:vagrant /home/vagrant/keycloak
         /home/vagrant/keycloak/bin/add-user-keycloak.sh -r master -u admin -p admin
  SHELL

  $up_script = <<-'SCRIPT'
    echo "Starting Keycloak"
    /home/vagrant/keycloak/bin/standalone.sh -b 0.0.0.0 \
    -Dkeycloak.migration.action=import \
    -Dkeycloak.migration.provider=singleFile \
    -Dkeycloak.migration.file=/vagrant/keycloak_config.json \
    -Dkeycloak.migration.strategy=OVERWRITE_EXISTING &> /dev/null &
    cd /vagrant/frontend-customers/
    echo "Starting angular for customers"
    ng serve --host 0.0.0.0 --port 31416 &> /dev/null &
    cd /vagrant/frontend-employees/
    echo "Starting angular for employees"
    ng serve --host 0.0.0.0 --port 42069 &> /dev/null &
    echo "Starting server"
    cd /vagrant/server
    /vagrant/server/mvnw spring-boot:run
  SCRIPT

  $halt_script = <<-'SCRIPT'
     killall -15 -r ^ng
  SCRIPT
  
  config.trigger.after :up do |trigger|
    trigger.info = "Starting the BadenAir server..."
    trigger.run_remote = {inline: $up_script}
  end

  config.trigger.before :halt do |trigger|
    trigger.info = "Killing the BadenAir server..."
    trigger.run_remote = {inline: $halt_script}
  end
end
