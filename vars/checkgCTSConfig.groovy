def call(Map args) {

    echo "Inside pullByCommit Function"
    def trkorr = ""
    def toolURL = 'https://'+args.hostname+':'+args.port+'/sap/bc/cts_abapvcs/system'
    echo 'tool url : '+toolURL	
    String ABAPURL = toolURL.toURL()
    //def resultJSON = 'results/ABAP_'+args.hostname+'_gCTS_result_'+env.BUILD_NUMBER+'.json'
    //def resp = httpRequest authentication: args.credentials, url: ABAPURL, outputFile: resultJSON
    def resp = httpRequest authentication: args.credentials, url: ABAPURL
    echo 'gCTS Config : '+ resp.content
    try{
	    def gCTSList = new groovy.json.JsonSlurper().parseText(resp.content)
      echo 'gCTSList object : '+gCTSList
      assert gCTSList instanceof Map
      assert gCTSList.result.config instanceof List
	    retrun True
     } 
     catch (exc)
      {
         echo 'xml parser failed'
         echo 'exception : '+exc
         return False
      }
}
