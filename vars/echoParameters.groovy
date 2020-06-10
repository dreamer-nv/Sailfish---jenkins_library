def call(Map args) {
	/*args.each{
	  echo "${args.key} = ${args.value}"
	}*/
	echo "Hostname = ${args.hostname}"
	echo "Port = ${args.port}"
	echo "Service = ${args.external_alias}"
	echo "TFC_ID = ${args.tfc_id}"
	echo "Transport = ${args.transport}"
	echo "Mail = ${args.mail}"
}