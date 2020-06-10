def call()
{
         
   def author_mail = sh (script: 'git --no-pager show -s --format=\'%ae\'',returnStdout: true).trim() //capturing git author email
   echo "Git Author email (GitHub Plugin): " + author_mail
   //author_mail = null // Some GitHub repositories are giving author mail as noreply@example.com
   if (author_mail == null || author_mail == " " || !author_mail.contains("@sap.com") || author_mail.contains("noreply")) {
   
     def git_author_name = sh (script: 'git --no-pager show -s --format=\'%an\'',returnStdout: true).trim() //capturing committed person name
     echo "Git author name is: "+git_author_name
     def xmlUrlString = "https://ldcipqe.wdf.sap.corp:44301/sap/opu/odata/BRLT/CI_PROJECT_SRV/UserDetailsSet?\$filter=i_num%20eq%20%27"+git_author_name+"%27"
     //echo 'XML URL is '+ xmlUrl
     String xmlUrl = xmlUrlString.toURL()
     def resp = httpRequest authentication: 'ARES_BUILD', url: xmlUrl
     def xmlString = resp.content
     echo 'Response: '+ xmlString
     def userSet = new XmlSlurper().parseText(xmlString)

     def mailID = userSet.'**'.find { node ->
         node.name() == 'email'
      }

     echo 'XML parser Gpath name '+ userSet.name()
     echo 'XML parser Gpath toString '+ userSet.toString()

     if(mailID == null || mailID.text() == "" || mailID.text() == " ") author_mail = "rajendra.lakshmi.prasad.b@sap.com" // Fall back approach to send mail to known admin. Currently hardcoded
         else author_mail = mailID.text()
     echo 'XML Parser : Mail ID (SAP Name / I / D Number to Mail Id) -> '+ author_mail
   }
   return author_mail
}
