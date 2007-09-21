<#function estPddDansListe pdd listPDD >
	<#local bRetour = false>
		<#list listPDD as pddReference >
	  		<#if pddReference.idPdd == pdd.idPdd>
				<#local bRetour = true>
				<#break>
			</#if>
	  	</#list>	
	<#return bRetour>
</#function>

<#function estVADansListe va listVA >
	<#local bRetour = false>
		<#list listVA as vaReference >
	  		<#if vaReference.idVoeuAmendement == va.idVoeuAmendement>
				<#local bRetour = true>
				<#break>
			</#if>
	  	</#list>	
	<#return bRetour>
</#function>

<#function estFichierDansListe fichier listFichiers >
	<#local bRetour = false>
		<#list listFichiers as fichierReference >
	  		<#if fichierReference.id == fichier.id>
				<#local bRetour = true>
				<#break>
			</#if>
	  	</#list>	
	<#return bRetour>
</#function>

<#function estOdjDansListe odj listOdj >
	<#local bRetour = false>
		<#list listOdj as odjReference >
	  		<#if odjReference.idOrdreDuJour == odj.idOrdreDuJour>
				<#local bRetour = true>
				<#break>
			</#if>
	  	</#list>	
	<#return bRetour>
</#function>