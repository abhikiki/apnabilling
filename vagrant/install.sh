#!/bin/sh

apt-get -y install dos2unix
#software add
mkdir /software
cp -R /vagrant/jdk8/* /software
cp -R /vagrant/tomcat8/* /software
cp -R /vagrant/restapi /software

tar -xvf /software/jdk-8u91-linux-x64.tar.gz -C /software/
tar -xvf /software/apache-tomcat-8.0.36.tar.gz -C /software/

cp /vagrant/webapp/* /software/apache-tomcat-8.0.36/webapps/
cp /software/restapi/application.properties /software/
cp /vagrant/autotomcat /etc/init.d
chmod 755 /etc/init.d/autotomcat
dos2unix /etc/init.d/autotomcat
update-rc.d autotomcat defaults

cp /vagrant/autorest /etc/init.d
chmod 755 /etc/init.d/autorest
dos2unix /etc/init.d/autorest
update-rc.d autorest defaults

#MYSQL
export DEBIAN_FRONTEND=noninteractive
apt-get update
apt-get -y install mysql-server
sed -i 's/bind-address/#bind-address/' /etc/mysql/my.cnf
mysqladmin --user=root --password= password 'root';
mysql --user=root --password=root < /vagrant/sql/testusercreation.sql
mysql --user=apna --password=apna < /vagrant/sql/tablesandprocsetup.sql
mysql --user=apna --password=apna < /vagrant/sql/insertshopanduserinfo.sql
mysql --user=apna --password=apna < /vagrant/sql/golditems.sql
mysql --user=apna --password=apna < /vagrant/sql/silveritems.sql
mysql --user=apna --password=apna < /vagrant/sql/generalitems.sql
mysql --user=apna --password=apna < /vagrant/sql/diamonditems.sql



