var req;

function createPerson(){
    var person = new Object();
    person.firstName = document.getElementById("nome").value;
    person.lastName = document.getElementById("cognome").value;
    person.id = document.getElementById("id").value;
    person.sex= document.getElementById("sesso").value;
    var url = "/rest/person/";
	req = new XMLHttpRequest( );
    req.open("POST",url,true);
    req.setRequestHeader("Content-Type", "text/plain");
    req.send(JSON.stringify(person));
    req.onreadystatechange = function(){
    	if(this.readyState==4){
    		div = document.getElementById("postResult");
    		url = this.getResponseHeader("Location");
    		if(this.status==201){
    			div.innerHTML="Persona creata con successo \n Indirizzo: "+url;
				div.style.display="inline-block";
    		}else{
    			div.innerHTML="Errore nell'inserimento";
				div.style.display="inline-block";
    		}
    	}
    }
}
function getPerson(){
	id = document.getElementById("getId").value;
	 var url = "/rest/person/"+id;
	 req = new XMLHttpRequest( );
	 req.open("GET",url,true);
	 req.send();
	 req.onreadystatechange = function(){
		 if(this.readyState==4){
			 div = document.getElementById("getResult");
			 if(this.status==200){
				 div.innerHTML=req.responseText;
				 div.style.display="inline-block";
			 }else if(this.status==404){
				 div.innerHTML="Persona non trovata";
				 div.style.display="inline-block";
			 }
			 
		 }
		
	 }
}
function putPerson(){
	 var person = new Object();
	    person.firstName = document.getElementById("putNome").value;
	    person.lastName = document.getElementById("putCognome").value;
	    person.id = document.getElementById("putId").value;
	    person.sex= document.getElementById("putSesso").value;
	    var url = "/rest/person/";
		req = new XMLHttpRequest( );
	    req.open("PUT",url,true);
	    req.setRequestHeader("Content-Type", "text/plain");
	    req.send(JSON.stringify(person));
	    console.log(person);
	    req.onreadystatechange = function(){
	    	if(this.readyState == 4){
	    		div = document.getElementById("putResult");
	    		if(this.status == 201){
	    			div.innerHTML="Persona creata con successo";
					 div.style.display="inline-block";
	    		}else if (this.status == 204){
	    			div.innerHTML="Persona aggiornata con successo";
					 div.style.display="inline-block";
	    		}
	    	}
	    }

}
function deletePerson(){
	id = document.getElementById("deleteId").value;
	 var url = "/rest/person/"+id;
	 req = new XMLHttpRequest( );
	 req.open("DELETE",url,true);
	 req.send();
	 req.onreadystatechange = function(){
		 if(this.readyState==4){
			 div = document.getElementById("deleteResult");
			 if(this.status==200){
				 div.innerHTML="Persona eliminata con successo";
				 div.style.display="inline-block";
			 }else if(this.status==404){
				 div.innerHTML="Persona non trovata!";
				 div.style.display="inline-block";
			 }else if(this.status==500){
				 div.innerHTML="Errore nel server!";
				 div.style.display="inline-block";
			 }
		 }
		 
	 }
}
/* 
var req;

function convertToDecimal( ) {
    var key = document.getElementById("key");
    var keypressed = document.getElementById("keypressed");
    keypressed.value = key.value;
    var url = "/hello/rest/hello/"+key.value;
    req = new XMLHttpRequest( );
    req.open("GET",url,true);
    req.setRequestHeader("Content-Type", "text/plain");
    //req.send(key.value)
    req.send();
    console.log(key.value);
    req.onreadystatechange = callback;
}

function callback() {
    if (req.readyState==4){
        if (req.status == 200) {
            //var decimal = document.getElementById('ascii');
            //decimal.value = req.responseText;
        	//document.getElementById('ascii').value = req.responseText.ascii;
        	response = JSON.parse(req.responseText);
        	console.log(response);
        	document.getElementById('ascii').value = response.ascii;
        	document.getElementById('hex').value = response.hex;
        	document.getElementById('octal').value = response.octal;
        	document.getElementById('binary').value = response.binary;
        }
    }
    clear( );
}
function clear() {
    var key = document.getElementById("key");
    key.value="";
}
function focusIn( ) {
    document.getElementById("username").focus( );
}
function validateUsername( ) {
    var username = document.getElementById("username");
    var url = "/hello2/rest/hello/" + username.value;
    req = new XMLHttpRequest( );
    req.open("GET",url,true);
    req.send()
    req.onreadystatechange = function(){
    	if (this.readyState == 4 && this.status == 200) {
    		usernameCheck( );
       }
    }
}
function usernameCheck( ) {
    // we only want a boolean back, so no parsing is necessary
    userExists = req.responseText;
    var element = document.getElementById("username");
    if (userExists == "false") {
        element.classList.add("usernameEsistente");
        document.getElementById("usernameAlertDiv").style.display = "inline";
        document.getElementById("usernameSuccessDiv").style.display = "none";
        username.focus( );
    }else{
        element.classList.remove("usernameEsistente");
        document.getElementById("usernameAlertDiv").style.display = "none";
        document.getElementById("usernameSuccessDiv").style.display = "inline";
    }
}
 */


