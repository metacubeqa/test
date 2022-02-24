#!/bin/bash

#exec 1> command.log 2>&1

read_package_array(){

	#versions for 1.83, 2.7 and 2.9 in sequence
	package_version_array=(04t6F0000045a4b 04t6F000000cgQM 04t6F000000cgZj)
	
	#add here latest stable version (3.96.32)
	package_version_array+=(04t6F000004PC1i)
	

	for i in "${package_version_array[@]}"
	do
		
		echo "getting version number"
		case $i in
				04t6F0000045a4b)
					version_number=1.83
					;;
				04t6F000000cgQM)
					version_number=2.7
					;;	
				04t6F000000cgZj)
					version_number=2.9
					;;
				04t6F000004PC1i)
					version_number=3.96.32
					;;
				*)  
					echo "default condition"
					version_number="undefined"	
					;; 	
		esac
		
		install_package

		run_collection
		
	done

}

install_package(){
	echo "Installing package with version number " $version_number{$i}
	yes | sfdx force:package:install -p $i -u gs_alias_automation4 -w 30 -b 30 2>&1 | tee logfile.txt
	
	
	if grep -q "Successfully installed package" logfile.txt
	then
   	 	echo "Package Successfully Installed..Continue"
	else
   		echo "Package Not Installed..Exiting"
		exit 1
	fi
}

run_collection(){

	echo "Running collection and creating report for " $version_number{$i}
	newman run Guided_Selling_collection.json -e PerfOrg_environment.json --reporters cli,htmlextra --reporter-htmlextra-title "GS Report package_version-$version_number{$i}" --reporter-htmlextra-export test-output/GS_Report_package-version_$version_number.html --reporter-htmlextra-titleSize 5
}

read_package_array
