%define debug_package %{nil}
%define __jar_repack  %{nil}
#%define _tmppath     %{_topdir}/tmp
%define jboss_home    /opt/CBKjboss
%define shared_dir    %{jboss_home}/keycloak-shared
%define config_dir    %{shared_dir}/configuration
%define log_dir       %{shared_dir}/log
%define user          ta2etok
%define rungrp        ta2etok

Name:           keycloak
Version:        3.1.0
Release:        59%{dist}
Summary:        Keycloak is an open source identity and access management solution.

Group:          Applications/System
Vendor:         Commerzbank AG (01-04-29)
Packager:       Tokenmanager Development Team <Tokenmanager@commerzbank.com>
License:        APLv2.0
URL:            http://www.keycloak.org/
Source0:        https://downloads.jboss.org/%{name}/%{version}.Final/%{name}-%{version}.Final.tar.gz
Source1:        keycloak.service
Source2:        keycloak.sysconfig
Source3:        LICENSE
Source4:        keycloak-ldap-coba-0.0.10-module.tar.gz
Source5:        configuration.tar.gz
Source6:        keycloak-clientloader-0.0.10-module.tar.gz
Source7:        keycloak-services-3.1.0.Final-coba-v1-module.tar.gz
Source8:        themes.tar.gz
Source9:        keycloak-tnv-0.0.10-module.tar.gz

Source11:       app.js
Source12:       users.js
Source13:       kc-tabs-ldap.html
Source14:       modules-oracle.tar.gz
Source15:       scripts.tar.gz


Prefix:         /opt

BuildRequires: systemd tar gzip

Requires(pre): shadow-utils
Requires:      systemd java-1.8.0-openjdk-headless

%description
Keycloak is an open source Identity and Access Management solution aimed at 
modern applications and services. It makes it easy to secure applications and 
services with little to no code.

%prep

%build

%install
# base software
install -d %{buildroot}%{jboss_home}/%{name}-%{version}
tar --strip-components=1 -C %{buildroot}%{jboss_home}/%{name}-%{version} -xvf %{SOURCE0}
ln -sf %{name}-%{version} %{buildroot}%{jboss_home}/%{name}
rm %{buildroot}%{jboss_home}/%{name}/bin/*.bat
rm %{buildroot}%{jboss_home}/%{name}/bin/*.ps1
# Patching keycloak-services module
rm %{buildroot}%{jboss_home}/%{name}/modules/system/layers/keycloak/org/keycloak/keycloak-services/main/*
tar -C %{buildroot}%{jboss_home}/%{name} -xvf %{SOURCE7}

# systemd service & shared dirs
install -D %{SOURCE1} %{buildroot}/%{_unitdir}/%{name}.service
install -D %{SOURCE2} %{buildroot}/%{_sysconfdir}/sysconfig/%{name}
install -D %{SOURCE3} %{buildroot}/%{_docdir}/%{name}-%{version}/LICENSE
mkdir -p %{buildroot}%{_sharedstatedir}/%{name}
mkdir -p %{buildroot}%{log_dir}
mkdir -p %{buildroot}%{shared_dir}/data
mkdir -p %{buildroot}%{shared_dir}/migration

# extensions
tar -C %{buildroot}%{jboss_home}/%{name} -xvf %{SOURCE4}
tar -C %{buildroot}%{jboss_home}/%{name} -xvf %{SOURCE6}
tar -C %{buildroot}%{jboss_home}/%{name} -xvf %{SOURCE9}
tar -C %{buildroot}%{jboss_home}/%{name} -xvf %{SOURCE14}

# configuration
mkdir -p %{buildroot}%{config_dir}
rm %{buildroot}%{jboss_home}/%{name}/standalone/configuration/standalone.xml
rm %{buildroot}%{jboss_home}/%{name}/standalone/configuration/standalone-ha.xml
mv %{buildroot}%{jboss_home}/%{name}/standalone/configuration/* %{buildroot}%{config_dir}
tar -C %{buildroot}%{config_dir} -xvf %{SOURCE5}

# UI contributions
tar -C %{buildroot}%{jboss_home}/%{name} -xvf %{SOURCE8}
rm -f %{buildroot}%{jboss_home}/%{name}/themes/base/admin/resources/js/app.js
install -D %{SOURCE11} %{buildroot}%{jboss_home}/%{name}/themes/base/admin/resources/js/app.js
rm -f %{buildroot}%{jboss_home}/%{name}/themes/base/admin/resources/js/controllers/users.js
install -D %{SOURCE12} %{buildroot}%{jboss_home}/%{name}/themes/base/admin/resources/js/controllers/users.js
rm -f %{buildroot}%{jboss_home}/%{name}/themes/base/admin/resources/templates/kc-tabs-ldap.html
install -D %{SOURCE13} %{buildroot}%{jboss_home}/%{name}/themes/base/admin/resources/templates/kc-tabs-ldap.html

# scripts
tar -C %{buildroot}%{jboss_home}/%{name}-%{version}/bin -xvf %{SOURCE15}


%pre
exit 0

%post
%systemd_post %{name}.service
case "$1" in
  1)
    # This is an initial install.
    # Add the first start admin user
    # %{jboss_home}/%{name}-%{version}/bin/add-user-keycloak.sh -r master -u admin -p initial
  ;;
  2)
    # This is an upgrade.
  ;;
esac

%preun
%systemd_preun %{name}.service

%postun
case "$1" in
  0)
    # This is an uninstallation.
  ;;
  1)
    # This is an upgrade.
  ;;
esac
%systemd_postun_with_restart %{name}.service

%clean
rm -rf %{buildroot}

%files
%defattr(-, %{user},%{rungrp},-)
%dir %attr(-, -, -) %{jboss_home}
%dir %attr(-, -, -) %{jboss_home}/%{name}-%{version}
%attr(-, -, -) %{jboss_home}/%{name}-%{version}/*
%attr(-, -, -) %{jboss_home}/%{name}
%attr(644, root, root) %{_unitdir}/%{name}.service
%config(noreplace) %attr(660, root, -) %{_sysconfdir}/sysconfig/%{name}
%doc %{_docdir}/%{name}-%{version}/LICENSE
%dir %attr(-, -, -) %{_sharedstatedir}/%{name}

# standalone folders
%dir %attr(775, -, -) %{jboss_home}/%{name}-%{version}/standalone
%dir %attr(775, -, -) %{jboss_home}/%{name}-%{version}/standalone/deployments

# shared folders
%dir %attr(-, -, -) %{shared_dir}
%dir %attr(770, -, -) %{shared_dir}/data
%dir %attr(775, -, -) %{log_dir}
%dir %attr(770, -, -) %{config_dir}
%dir %attr(770, -, -) %{shared_dir}/migration
%config(noreplace) %attr(664, -, -) %{config_dir}/*
%config(noreplace) %attr(660, -, -) %{config_dir}/keystore.jks
%config(noreplace) %attr(660, -, -) %{config_dir}/client.jks
%config(noreplace) %attr(660, -, -) %{config_dir}/realm-keys-tokenmanager.jks
%config(noreplace) %attr(660, -, -) %{config_dir}/keycloak.properties


%changelog
* Thu Jan 18 2018 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-59
- Added null check in tnv configuration
* Thu Jan 18 2018 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-58
- Updated privileged port binding config as it seems that system is too old
* Thu Jan 18 2018 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-57
- Updated ldap intergation to set coba sepcific user attributes
- Updated tnv intergation to set coba sepcific user attributes
- Updated tnv integration configuration
- Changed the way to allow privileged port binding again, back to systemd
* Wed Jan 17 2018 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-56
- Changed the way to allow privileged port binding
* Wed Jan 17 2018 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-55
- Added systemd capabilities to allow privileged port binding
* Wed Jan 17 2018 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-54
- Prevented collisions in client loader for duplicate ids
* Mon Jan 15 2018 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-53
- Added capabilities to do client authentication for rest calls
* Fri Jan 12 2018 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-52
- Fixed RPM spec typo of added source
* Fri Jan 12 2018 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-51
- Added TNV user integration for internal realm
* Wed Jan 10 2018 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-50
- Added delete capabilities to client loader
* Wed Jan 10 2018 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-49
- Upgraded extension libs after restructuring
* Mon Dec 11 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-48
- Replaced certificate to support new DNS names
* Fri Dec 08 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-47
- Updated configuration to support clustering via tcp
* Wed Nov 29 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-46
- Updated client loader to version 0.0.2
- Added coba theme
* Fri Nov 17 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-45
- Replaced keycloak-services with patched version to allow more flexible configuration import
* Wed Nov 15 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-44
- Added automatic client loading for axway integration
* Fri Nov 03 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-43
- Changed rights of deployment folder
- Updated environemnt variables in scripts
* Fri Nov 03 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-42
- Changed line endings
* Thu Nov 02 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-41
- Rebuild because of rpm build problems with git history
* Thu Nov 02 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-40
- Changed RPM build process to use archives for file bundles
* Wed Nov 01 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-39
- Reworked RPM packaging
* Mon Oct 30 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-38
- Fixed SPEC which is still not working
* Mon Oct 30 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-37
- Fixed SPEC files definition for script executablity
* Mon Oct 30 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-36
- Fixed script solution for source packaging
* Fri Oct 27 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-35
- Added import and database migration script
- Removed unneeded windows scripts
* Mon Oct 23 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-34
- Switched database migration mode to validate
- Changed umask for service process to get files writable to group
* Fri Oct 20 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-33
- Added oracle jdbc driver module
* Thu Oct 19 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-32
- Fixed file attributes
* Mon Oct 16 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-31
- Exchanged certificates for all stages
- Switched database setup configuration to manual
* Tue Oct 10 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-30
- Migrated spec to global ldap tecnical user
* Fri Sep 29 2017 Sebastian Titakis <sebastian.titakis@commerzbank.com> - 3.1.0-29
- Added UI custom files (users.js, kc-tabs-ldap.html and new app.js version) to handle 'ldapcbk' properly
* Tue Sep 26 2017 Sebastian Titakis <sebastian.titakis@commerzbank.com> - 3.1.0-28
- Added app.js to custom files and updated user sub extension to 0.0.3
* Wed Sep 13 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-27
- Updated user sub extenstion to 0.0.2
* Fri Sep 01 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-26
- Split up start parameters in mltiple arguments as they are not parsed correct as one
* Thu Aug 31 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-25
- Added config location path to sysconfig
* Thu Aug 31 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-24
- Fixed configuration location in service config and moved missing config files
* Thu Aug 31 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-23
- Fixed configuration location in service config
* Thu Aug 31 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-22
- Set changed logdir in service config
* Thu Aug 31 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-21
- Removed migration of database from script
* Wed Aug 30 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-20
- Added missing keycloak.properties file
* Wed Aug 30 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-19
- Fixed configuration file rights
* Wed Aug 30 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-18
- Merged with configuration rpm
- Added shared data dirs
- Added cluster configuration file
* Fri Aug 04 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-17
- Added custom provider
* Fri Jul 07 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-16
- Fixed exclusion of standalone.xml file
* Thu Jul 06 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-15
- Extracted configuration into separate rpm
* Wed Jul 05 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-14
- Removed development hooks
* Wed Jul 05 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-13
- Fixed address binding syntax for service
* Wed Jul 05 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-12
- Fixed invalid start option
* Wed Jul 05 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-11
- Created workdirectory during install
- Yet another ore file rights fix
* Wed Jul 05 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-10
- Fixed some more file rights
* Tue Jul 04 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-9
- Fixed file rights
* Tue Jul 04 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-8
- Added logging
* Tue Jul 04 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-7
- Updated certificate to official issued one
- Changed file ownership back to service specific
- Added service start logging for debug purpose
* Fri Jun 30 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-6
- Changed file ownership to personal user to get app running
* Fri Jun 30 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-5
- Fixed handling of runtime users in case of upgrade
* Fri Jun 30 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-4
- Fixed link in install directory
- Added service activation
- Added some testing hooks
* Fri Jun 30 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-3
- Some slight corrections in spec metadata
* Thu Jun 29 2017 Fabian Schlier <fabian.schlier@commerzbank.com> - 3.1.0-2
- Reworked package to align to Commerzbank needs
* Thu Jun 01 2017 Arun Babu Neelicattu <arun.neelicattu@gmail.com> - 3.1.0-1
- Initial packaing source.

