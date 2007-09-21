function toggleBoxes(f, dowhat) {
	for (i=0; i<f.elements.length; i++) {
		if (f.elements[i].type == 'checkbox') {
			if (dowhat == 'on') {
				f.elements[i].checked = true;
			} 
			else {
				f.elements[i].checked = false;
			}
		}
	}
}

function toggleBoxesPanier(f, dowhat) {
	var tailleTotal = document.getElementById('tailleTotal');
	
	for (i=0; i<f.elements.length; i++) {
		if (f.elements[i].type == 'checkbox') {
			if (dowhat == 'on') {
				f.elements[i].checked = true;
				tailleTotal.value = parseInt(tailleTotal.value) + parseInt(document.getElementById(f.elements[i].value).value);
			} 
			else {
				f.elements[i].checked = false;
				tailleTotal.value = 0;
			}
		}
	}
}

function downloadPanier(f, urlBase, tailleTotalMax) {
	var tailleTotal = document.getElementById('tailleTotal');
	
	if( parseInt(tailleTotal.value) <= parseInt(tailleTotalMax) )
	{
		var flagIsFoundOne = false;
		for (i=0; i<f.elements.length; i++) 
		{
			if (f.elements[i].type == 'checkbox')
			 {
				if ( f.elements[i].checked ) 
				{
					flagIsFoundOne = true;
				} 
			}
		}
		
		if( flagIsFoundOne )
		{
			var timeDownload = new Date().getTime();
			f.dateAjout.value = timeDownload;
			f.submit();
			var ods_win_telechargement = self.open(urlBase+'&dateAjout='+timeDownload, 'ods_win_telechargement', 'toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbar=yes,resizable=yes,copyhistory=yes,width=500px,height=300px');
			ods_win_telechargement.focus();
		}
	}
	else
	{
		var ods_win_telechargement = self.open(urlBase+'&dateAjout=null', 'ods_win_telechargement', 'toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbar=yes,resizable=yes,copyhistory=yes,width=500px,height=300px');
		ods_win_telechargement.focus();
	}
}

function manage(obj, taille )
{
    var tailleTotal = document.getElementById('tailleTotal');
    
    if( obj.checked ){
      tailleTotal.value = parseInt(tailleTotal.value) + parseInt(taille);
    }else{
      tailleTotal.value = parseInt(tailleTotal.value) - parseInt(taille);
    }
    
}

function displayId(baliseId)
{
	if (document.getElementById && document.getElementById(baliseId) != null) {
		document.getElementById(baliseId).style.visibility='visible';
		document.getElementById(baliseId).style.display='block';
	}
}

function hideId(baliseId) {
	if (document.getElementById && document.getElementById(baliseId) != null) {
   		document.getElementById(baliseId).style.visibility='hidden';
    	document.getElementById(baliseId).style.display='none';
	}
}
  
function getElementById(id) {
	if (document.getElementById) return document.getElementById(id);
	if (document.all) return eval("document.all."+id);
	return null;
}

function display(id, value) {
	var panel = getElementById(id);
	panel.style.display = value;
}