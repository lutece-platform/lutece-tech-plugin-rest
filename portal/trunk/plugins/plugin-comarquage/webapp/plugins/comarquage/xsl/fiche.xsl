<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:lutece="luteceComarquage" >

<xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0" omit-xml-declaration="yes"/>
<!-- ************************************************************************ -->
<!-- Formatage en Chapitre, Paragraphe, ... inclus dans le fichier texte.xsl -->
<xsl:include href="texte.xsl" />

<xsl:param name="selectedCdcCode"></xsl:param>
<xsl:param name="pluginName"></xsl:param>

<!-- variable d'affichage de la puce pour une liste -->
<xsl:variable name="puce" >
	<img src="images/local/skin/bullet.gif" align="middle" alt="" /><xsl:text> </xsl:text>
</xsl:variable>

<!-- le nom des styles à utiliser -->
<xsl:variable name="portletStyle" select="'portlet'" />
<xsl:variable name="portletHeaderStyle" select="'portlet-header'" />
<xsl:variable name="portletContentStyle" select="'portlet-content'" />

<!-- ************************************************************************ -->
<!-- nom du répertoire courant dans l'aborescence cdc -->
<xsl:variable name="cdcBaseUrl" >
	<xsl:text>http://www.servicepubliclocal.com/spl</xsl:text>
</xsl:variable>


<!-- ************************************************************************ -->
<!-- Variable globale : référence jusqu'au thème parent,
utilisée pour construire les liens -->
<xsl:variable name="lien_chemin">
	<xsl:value-of select="/fiche_localisee/infos_techniques/chemin/@id"/>
	<xsl:text>/</xsl:text>
</xsl:variable>


<!-- ************************************************************************ -->
<!-- un tableau de 2 colonnes qui contient toutes les infos -->
<xsl:template match="/fiche_localisee">




	<p style="text-align:left;margin-left:10px" >
		<xsl:apply-templates select="infos_techniques/chemin"/>
	</p>

	<p style="text-align:right;margin-right:10px">
		<a href="JavaScript:print()">
			<img src="images/local/skin/buttons/b_print.gif" border="0" alt="Imprimer" />
		</a>
	</p>


   	<div id="two-zones-first">
	<div id="two-zones-first-content">
   	<!-- 1ere colonne / contenu principal -->


		<xsl:apply-templates select="fiche_pratique"/>

	</div>
	</div>

	<div id="two-zones-second">
	<div id="two-zones-second-content">
	<!-- 2eme colonne -->




		<xsl:call-template name="source"/>
		<xsl:call-template name="ressources"/>
	</div>
	</div>


</xsl:template>



<!-- ************************************************************************ -->
<!-- Colonne de gauche : contient la zone principale "vosdroits" dans une portlet,
puis la zone "vosquestions" et les liens chacun dan une portlet -->
<xsl:template match="fiche_pratique">

	<div class="{$portletStyle}">

		<h3 class="{$portletHeaderStyle}">
			<xsl:value-of select="titre"/>
		</h3>

		<div class="{$portletContentStyle}">
			<xsl:apply-templates select="vosdroits"/>
		</div>

	</div>


	<xsl:apply-templates select="flash"/>
	<xsl:apply-templates select="vosquestions"/>
	<xsl:apply-templates select="liens"/>
	<xsl:call-template name="coordonnees_locales"/>

</xsl:template>


 <!-- Template "vosdroits"
 contient les noeuds_fils (liens vers sous-thèmes/fiches ou lien dans la page)
 et les éléments "fiches" si la page est de type fiche -->
 <xsl:template match="vosdroits">
	<xsl:apply-templates select="noeuds_fils"/>
	<xsl:if test="(count(child::fiches)!=0) and (count(child::noeuds_fils)!=0) ">
		<hr/><br/>
	</xsl:if>
	<xsl:apply-templates select="fiches"/>
</xsl:template>

 <!-- Template "fiches" -->
 <xsl:template match="fiches">
 	<xsl:apply-templates select="fiche"/>
</xsl:template>



<!-- ************************************************************************ -->
<!-- Template "source"
permet de créer la portlet en haut à droite qui donne
 les infos sur la source des pages et leur date de mise à jour -->
<xsl:template name="source">

	<div class="{$portletStyle}">

		<h3 class="{$portletHeaderStyle}">

			Source
		</h3>
		<div class="{$portletContentStyle}">


			<a href="http://www.service-public.fr/" title="Site service-public.fr" >
				<img alt="Lien vers le site service-public.fr" src="images/local/skin/plugins/comarquage/logo_service_public.jpg" border="0"  />
			</a>
			<br/>
			Dernière mise à jour le <xsl:value-of select="fiche_pratique/date_update"/>

		</div>

	</div>

</xsl:template>


<!-- ************************************************************************ -->
<!-- Template chemin
Affiche la hiérarchie des thèmes jusqu'à l'élément courant, pour permettre la navigation -->
<xsl:template match="chemin">
		<font class="path">

		<xsl:for-each select="fiche_tech">

		<xsl:if test="position()=2 and position()!=last()">
			<a href="jsp/site/Portal.jsp?page={$pluginName}&amp;id={@path}"><xsl:value-of select="@titre"/></a>
		</xsl:if>

		</xsl:for-each>
		</font>
</xsl:template>



<!-- ************************************************************************ -->
<!-- Template ressources
les infos qui sont ajoutées dans la colonne de droite sous forme de portlet-->
<xsl:template name="ressources">


	<xsl:apply-templates select="fiche_pratique/tele_procedures"/>


	<xsl:apply-templates select="fiche_pratique/tele_services"/>

	<xsl:call-template name="teleservices"/>
	<xsl:call-template name="teleformulaires"/>

	<!--
	<xsl:apply-templates select="fiche_pratique/formulaires"/>
	-->

	<xsl:apply-templates select="infos_loc"/>

	<xsl:apply-templates select="fiche_pratique/references"/>

	<xsl:apply-templates select="fiche_pratique/definitions"/>

	<xsl:apply-templates select="fiche_pratique/lettres"/>



</xsl:template>



<!-- ************************************************************************ -->
<!-- Template "formulaires" -->
<xsl:template match="formulaires">
	<div class="{$portletStyle}">

		<h3 class="{$portletHeaderStyle}">

			<xsl:text>Formulaires</xsl:text>

		</h3>
		<div class="{$portletContentStyle}">

			<xsl:apply-templates select="formulaire"/>

		</div>
	</div>
</xsl:template>


<!-- Template "formulaire" -->
<xsl:template match="formulaire">
	<p><xsl:copy-of select="$puce" />

		<a target="_blank" href="{lien}"><xsl:value-of select="titre"/></a>
		<xsl:apply-templates select="description"/>

	</p>
</xsl:template>



<!-- ************************************************************************ -->
<!-- Template "tele_procedures" -->
<xsl:template match="tele_procedures">
	<div class="{$portletStyle}">

		<h3 class="{$portletHeaderStyle}">

			<xsl:text>Téléprocédures</xsl:text>

		</h3>
		<div class="{$portletContentStyle}">

			<xsl:apply-templates select="tele_procedure"/>

		</div>
	</div>
</xsl:template>



<!-- Template "tele_procedure" -->
<xsl:template match="tele_procedure">
	<p><xsl:copy-of select="$puce" />

		<a target="_blank" href="{lien}"><xsl:value-of select="titre"/></a>
		<xsl:apply-templates select="description"/>

	</p>
</xsl:template>


<!-- ************************************************************************ -->
<!-- Template "tele_services" -->
<xsl:template match="tele_services">
	<div class="{$portletStyle}">

		<h3 class="{$portletHeaderStyle}">

			<xsl:text>Téléservices</xsl:text>

		</h3>
		<div class="{$portletContentStyle}">

			<xsl:apply-templates select="tele_service"/>

		</div>
	</div>
</xsl:template>



<!-- Template "tele_service" -->
<xsl:template match="tele_service">
	<p><xsl:copy-of select="$puce" />

		<a target="_blank" href="{lien}"><xsl:value-of select="titre"/></a>
		<xsl:apply-templates select="description"/>

	</p>
</xsl:template>




<!-- ************************************************************************ -->
<!-- Template "references" -->
<xsl:template match="references">

<div class="{$portletStyle}">
		<h3 class="{$portletHeaderStyle}">

			<xsl:text>Textes de référence</xsl:text>

		</h3>
		<div class="{$portletContentStyle}">

			<xsl:apply-templates select="reference"/>

		</div>
</div>

</xsl:template>

<!-- Template "reference" -->
<xsl:template match="reference">
	<p><xsl:copy-of select="$puce" />

		<a target="_blank" href="{lien}"><xsl:value-of select="titre"/></a>
		<xsl:apply-templates select="description"/>
	</p>
</xsl:template>




<!-- ************************************************************************ -->
<!-- Template "definitions" -->
<xsl:template match="definitions">

<div class="{$portletStyle}">
		<h3 class="{$portletHeaderStyle}">

			<xsl:text>Définitions</xsl:text>

		</h3>
		<div class="{$portletContentStyle}">


			<xsl:apply-templates select="definition"/>

		</div>
</div>

</xsl:template>

<!-- Template "definition" -->
<xsl:template match="definition">
	<p id="{@id}"><xsl:copy-of select="$puce" />

		<a target="_blank" href="{lien}"><xsl:value-of select="titre"/></a>
		<xsl:apply-templates select="description"/>
	</p>
</xsl:template>



<!-- ************************************************************************ -->
<!-- Template "lettres" -->
<xsl:template match="lettres">

<div class="{$portletStyle}">
		<h3 class="{$portletHeaderStyle}">

			<xsl:text>Lettre types</xsl:text>

		</h3>
		<div class="{$portletContentStyle}">


			<xsl:apply-templates select="lettre"/>

		</div>
</div>

</xsl:template>

<!-- Template "lettre" -->
<xsl:template match="lettre">
	<p><xsl:copy-of select="$puce" />
		<b><xsl:value-of select="titre"/></b>
		<xsl:apply-templates select="description"/>
	</p>
</xsl:template>



<!-- ************************************************************************ -->
<!-- Template "vosquestions" -->
<xsl:template match="vosquestions">

	<div class="{$portletStyle}">

		<h3 class="{$portletHeaderStyle}">

			<xsl:text>Questions / réponses</xsl:text>

		</h3>
		<div class="{$portletContentStyle}">

			<xsl:apply-templates select="item"/>

		</div>
	</div>

</xsl:template>

<!-- ************************************************************************ -->
<!-- Template "flash" -->
<xsl:template match="flash">

	<div class="{$portletStyle}">

		<h3 class="{$portletHeaderStyle}">

			<xsl:text>Flash-infos</xsl:text>

		</h3>
		<div class="{$portletContentStyle}">

			<p id="{@id}"><xsl:copy-of select="$puce" />

				<b><xsl:value-of select="titre"/></b>
				<xsl:apply-templates select="description"/>
			</p>

		</div>
	</div>

</xsl:template>


<!-- ************************************************************************ -->
<!-- Template "coordonnees_locales" -->
<xsl:template name="coordonnees_locales">

	<xsl:if test="count(//details_competence[normalize-space(.)])>0">
	<div class="{$portletStyle}">

		<h3 class="{$portletHeaderStyle}">

			<xsl:text>Complément d'information locale</xsl:text>

		</h3>
		<div class="{$portletContentStyle}">


			<xsl:apply-templates select="//details_competence"/>

		</div>
	</div>
	</xsl:if>

</xsl:template>



<!-- ************************************************************************ -->
<!-- Template "info_locale" -->
<xsl:template match="liens">

	<div class="{$portletStyle}">

		<h3 class="{$portletHeaderStyle}">

			<xsl:text>Pour en savoir plus</xsl:text>

		</h3>
		<div class="{$portletContentStyle}">

			<xsl:apply-templates/>

		</div>

	</div>

</xsl:template>

<!-- Template "web" -->
<xsl:template match="web">
	<h3>Web :</h3>
	<xsl:apply-templates select="item"/>
</xsl:template>

<!-- Template "minitel" -->
<xsl:template match="minitel">
	<h3>Minitel :</h3>

	<xsl:apply-templates select="item"/>
</xsl:template>



<!-- ************************************************************************ -->
<!-- Template "description" -->
<xsl:template match="description">
	<br/><xsl:apply-templates />
</xsl:template>

<!-- Template "lien_desc" -->
<xsl:template match="lien_desc">
	<a href="{@href}" target="{@target}" ><xsl:value-of select="."/></a><br/>
</xsl:template>

<!-- Template "noeuds_fils" -->
<xsl:template match="noeuds_fils">
	<xsl:apply-templates select="item"/>
	<br/>
</xsl:template>

<!-- Template "noeuds_fils/item" -->
<xsl:template match="noeuds_fils/item">

<p><xsl:copy-of select="$puce" />

	<xsl:choose>

	  <xsl:when test="count(child::lien)!=0">
		<a href="jsp/site/Portal.jsp?page={$pluginName}&amp;id={$lien_chemin}{lien}"><xsl:value-of select="titre"/></a>

	  </xsl:when>

	  <xsl:when test="count(child::ancre)!=0">
	  	<a href="jsp/site/Portal.jsp?page={$pluginName}&amp;id={$lien_chemin}{ancre}"><xsl:value-of select="titre"/></a>
	  </xsl:when>

	  <xsl:when test="count(child::code)!=0">
	  	<a href="jsp/site/Portal.jsp?page={$pluginName}&amp;id={$lien_chemin}#"><xsl:value-of select="titre"/></a>
	  </xsl:when>

	  <xsl:otherwise>
	  	<a href="jsp/site/Portal.jsp?page={$pluginName}&amp;id={$lien_chemin}#"><xsl:value-of select="titre"/></a>
	  </xsl:otherwise>

	</xsl:choose>

	<xsl:apply-templates select="description"/>

	<br/><xsl:apply-templates select="Texte"/>
</p>

</xsl:template>

<!-- Template "vosquestions/item" -->
<xsl:template match="vosquestions/item">
	<p><xsl:copy-of select="$puce" />
		<a href="jsp/site/Portal.jsp?page={$pluginName}&amp;id={$lien_chemin}{lien}"><xsl:value-of select="titre"/></a>
		<xsl:apply-templates select="description"/>
		<br/><xsl:apply-templates select="Texte"/>
	</p>
</xsl:template>

<!-- Template "liens/web/item" -->
<xsl:template match="liens/web/item">
	<p><xsl:copy-of select="$puce" />
		<a target="_blank" href="{lien}"><xsl:value-of select="titre"/></a>
		<xsl:apply-templates select="description"/>
		<br/><xsl:apply-templates select="Texte"/>
	</p>
</xsl:template>




<!-- ************************************************************************ -->
<!-- Template "infos_loc" -->
<xsl:template match="/fiche_localisee/infos_loc">

	<xsl:apply-templates select="services_accomplir" />
	<xsl:apply-templates select="services_informer" />

</xsl:template>


<!-- ************************************************************************ -->
<!-- Template "services_informer" -->
<xsl:template match="services_informer">

    <!-- count the number of service that will be shown -->
    <xsl:variable name="nShow">
   	 <xsl:value-of select="count(service[not(@lutece:code_cdc) or not(normalize-space(@lutece:code_cdc)) or @lutece:code_cdc=$selectedCdcCode ])"/>
    </xsl:variable>

   <!-- if we have something to show, display the content box -->
   <xsl:if test="not($nShow=0)">


		<div class="{$portletStyle}">

			<h3 class="{$portletHeaderStyle}">

				<xsl:text>Pour vous informer</xsl:text>

			</h3>

			<div class="{$portletContentStyle}">

				<xsl:apply-templates select="service" />

			</div>
		</div>
   </xsl:if>

</xsl:template>


<!-- ************************************************************************ -->
<!-- Template "services_accomplir" -->
<xsl:template match="services_accomplir">

    <!-- count the number of service that will be shown -->
   <xsl:variable name="nShow">
	   <xsl:value-of select="count(service[not(@lutece:code_cdc) or not(normalize-space(@lutece:code_cdc)) or @lutece:code_cdc=$selectedCdcCode ])"/>
   </xsl:variable>


   <!-- if we have something to show, display the content box -->
   <xsl:if test="not($nShow=0)">

		<div class="{$portletStyle}">

			<h3 class="{$portletHeaderStyle}">

				<xsl:text>Pour accomplir votre démarche</xsl:text>

			</h3>
			<div class="{$portletContentStyle}">

				<xsl:apply-templates select="service" />

			</div>
		</div>
    </xsl:if>

</xsl:template>




<!-- Template "service" -->

<!-- if no code_cdc given, display -->
<xsl:template match="service[not(@lutece:code_cdc)]">
	<xsl:apply-templates select="point_d_accueil"/>
	<xsl:apply-templates select="numero_d_appel"/>
	<xsl:apply-templates select="contact_usager"/>
	<xsl:apply-templates select="a_savoir"/>
</xsl:template>

<!-- if code_cdc empty, display -->
<xsl:template match="service[not(normalize-space(@lutece:code_cdc))]">
	<xsl:apply-templates select="point_d_accueil"/>
	<xsl:apply-templates select="numero_d_appel"/>
	<xsl:apply-templates select="contact_usager"/>
	<xsl:apply-templates select="a_savoir"/>
</xsl:template>
<!-- if code_cdc empty is the same has the one given, display -->
<xsl:template match="service[@lutece:code_cdc=$selectedCdcCode]">
	<xsl:apply-templates select="point_d_accueil"/>
	<xsl:apply-templates select="numero_d_appel"/>
	<xsl:apply-templates select="contact_usager"/>
	<xsl:apply-templates select="a_savoir"/>
</xsl:template>

<!-- other cases : dont't display -->
<xsl:template match="service" />



<!-- ************************* -->
<xsl:template name="teleservices">

<xsl:if test="count(//teleservice)>0">
<div class="{$portletStyle}">

		<h3 class="{$portletHeaderStyle}">

			<xsl:text>Téléservices</xsl:text>

		</h3>
		<div class="{$portletContentStyle}">

			<xsl:apply-templates select="//teleservice"/>

		</div>
</div>
</xsl:if>
</xsl:template>
<!-- ************************* -->
<xsl:template name="teleformulaires">

<xsl:if test="count(//teleformulaire)+count(//formulaires/formulaire)>0">
<div class="{$portletStyle}">

		<h3 class="{$portletHeaderStyle}">

			<xsl:text>Formulaires en ligne</xsl:text>

		</h3>
		<div class="{$portletContentStyle}">

			<xsl:apply-templates select="//teleformulaire"/>
			<xsl:apply-templates select="//formulaire"/>

		</div>
	</div>
</xsl:if>
</xsl:template>

<!-- ************************* -->
<!-- Template "teleservice" -->
<xsl:template match="teleservice">

	<p><xsl:copy-of select="$puce" />
	<a target="_blank" href="{url}">
		<xsl:value-of select="nom"/>
	</a>
	<xsl:apply-templates select="description_url"/>

	<xsl:apply-templates select="organisme/source" />
	</p>
</xsl:template>


<!-- ************************* -->
<!-- Template "teleformulaire" -->
<xsl:template match="teleformulaire">
	<p><xsl:copy-of select="$puce" />

		<a target="_blank" href="{url}">
			<xsl:value-of select="nom"/>
		</a>
		<xsl:apply-templates select="description_url"/>

		<xsl:apply-templates select="organisme/source" />
		<!--<xsl:apply-templates select="details_competence"/>-->

	</p>
</xsl:template>

<xsl:template match="description_url">
	<br/>
	<xsl:call-template name="substitute_retour_ligne">
		<xsl:with-param name="chaine" select="."/>
	</xsl:call-template>

</xsl:template>






<!-- ************************* -->
<!-- Template "contact_usager" -->
<xsl:template match="contact_usager">
<ul><li>Contact<br/>

	<xsl:apply-templates select="nom"/>
	<xsl:apply-templates select="telephone"/>
	<xsl:apply-templates select="telecopie"/>
	<xsl:apply-templates select="bureau"/>
	<xsl:apply-templates select="etage"/>
</li></ul>
</xsl:template>

<!-- Template "nom" -->
<xsl:template match="nom">

	<b>
		<xsl:call-template name="substitute_retour_ligne">
		  <xsl:with-param name="chaine" select="."/>
		</xsl:call-template>
	</b>
</xsl:template>


<!-- Template "telephone" -->
<xsl:template match="telephone">
	<br/>Tél. :
	<xsl:text> </xsl:text>
	<xsl:call-template name="format_number">
		  <xsl:with-param name="chaine" select="."/>
	</xsl:call-template>
</xsl:template>

<!-- Template "telecopie" -->
<xsl:template match="telecopie">
	<br/>Fax :
	<xsl:text> </xsl:text>
	<xsl:call-template name="format_number">
		  <xsl:with-param name="chaine" select="."/>
	</xsl:call-template>
</xsl:template>
<!-- Template "bureau" -->
<xsl:template match="bureau">
	<br/>Bureau : <xsl:value-of select="."/>
</xsl:template>
<!-- Template "etage" -->
<xsl:template match="etage">
	<br/>Etage : <xsl:value-of select="."/>
</xsl:template>


<!-- ************************* -->
<!-- Template "a_savoir" -->
<xsl:template match="a_savoir">
<ul><li>A savoir<br/>
	<xsl:value-of select="."/>
</li></ul>
</xsl:template>




<!-- ************************* -->
<!-- Template "source" -->
<xsl:template match="source">


	<br/>
	<xsl:if test="url_logo_medaillon">
	<br/>
		<a target="_blank" href="{url}" >
			<img src="{$cdcBaseUrl}{url_logo_medaillon}" border="0"/>
		</a>
	</xsl:if>
	<br/><xsl:text>Source : </xsl:text><xsl:value-of select="nom"/>

</xsl:template>


<!-- ************************* -->
<!-- Template "point_d_accueil" -->
<xsl:template match="point_d_accueil">

<xsl:variable name="tester_nom">
      <xsl:value-of select="nom" />
</xsl:variable>
<xsl:variable name="nouveau" select="'Nouveau Service'" />
<xsl:if test="not(contains($tester_nom,$nouveau))">

                <p><xsl:copy-of select="$puce" />
		<xsl:apply-templates select="organisme/nom"/>
		<br/><xsl:apply-templates select="nom"/>

		<xsl:if test="normalize-space(adresse_postale)">
		<br/><xsl:apply-templates select="adresse_postale"/>
		</xsl:if>

		<xsl:if test="normalize-space(coord_num)">
		<xsl:apply-templates select="coord_num"/>
		<br />
		</xsl:if>

		<xsl:if test="count(ouverture) > 0 ">

		 <br />
    		<b>Horaires</b><br />
    		<xsl:apply-templates select="ouverture[not(condition_ouverture)]"/>
		<xsl:apply-templates select="ouverture[condition_ouverture]"/>

		</xsl:if>
		<xsl:if test="normalize-space(coord_num/url)">
		<xsl:apply-templates select="coord_num/url"/>
		<!--<br/><xsl:apply-templates select="details_competence"/>-->
		</xsl:if>
		<xsl:apply-templates select="organisme/source" />
   
</p><br/>
</xsl:if>
</xsl:template>

<!-- Template "adresse_postale" -->
<xsl:template match="adresse_postale">
	<br/>
	<xsl:apply-templates select="position_voie"/>
	<xsl:apply-templates select="complement_adresse"/>
	<xsl:apply-templates select="bp"/>
	<xsl:apply-templates select="cp"/>
	<xsl:apply-templates select="ville"/>
	<xsl:apply-templates select="cedex"/>
</xsl:template>

<!-- Template "position_voie" -->
<xsl:template match="position_voie">
	<xsl:value-of select="."/><br/>
</xsl:template>
<!-- Template "complement_adresse" -->
<xsl:template match="complement_adresse">
	<xsl:value-of select="."/><br/>
</xsl:template>
<!-- Template "bp" -->
<xsl:template match="bp">
	<xsl:if test="current()!=''">
		<xsl:value-of select="."/><br/>
	</xsl:if>
</xsl:template>
<!-- Template "cp" -->
<xsl:template match="cp">
	<xsl:value-of select="."/><xsl:text> </xsl:text>
</xsl:template>
<!-- Template "ville" -->
<xsl:template match="ville">
	<xsl:value-of select="."/><xsl:text> </xsl:text>
</xsl:template>
<!-- Template "cedex" -->
<xsl:template match="cedex">
	<xsl:value-of select="."/>
</xsl:template>
<!-- Template "url_plan_acces" -->
<xsl:template match="url_plan_acces">
	<br/><a target="_blank" href="{.}">Plan d'accès</a>
</xsl:template>

<!-- Template "coord_num" -->
<xsl:template match="coord_num">

	<xsl:apply-templates select="telephone"/>
	<xsl:apply-templates select="telecopie"/>
	<xsl:apply-templates select="minitel_loc"/>
	<!-- <xsl:apply-templates select="url"/> -->
	<xsl:apply-templates select="email"/>
	<xsl:apply-templates select="wap"/>
</xsl:template>

<!-- Template "minitel_loc" -->
<xsl:template match="minitel_loc">
 	<br/>Minitel : <xsl:value-of select="."/>
</xsl:template>
<!-- Template "url" -->
<xsl:template match="url">
 	<br/>Site internet :
 	<xsl:text> </xsl:text>
 	<a target="_blank" href="{.}"><xsl:value-of select="."/></a>
</xsl:template>
<!-- Template "email" -->
<xsl:template match="email">
 	<br/>Email : <a href="mailto:{.}"><xsl:value-of select="."/></a>
</xsl:template>
<!-- Template "wap" -->
<xsl:template match="wap">
 	<br/>Wap : <xsl:value-of select="."/>
</xsl:template>

<!-- Template "ouverture" -->
<xsl:template match="ouverture">
	<xsl:apply-templates select="condition_ouverture"/>
	<xsl:apply-templates select="type_ouverture"/>
	<xsl:apply-templates select="plage_j"/>
</xsl:template>



<!-- Template "condition_ouverture" -->
<xsl:template match="condition_ouverture">
 	<xsl:value-of select="."/>
 	<xsl:text>, </xsl:text>
</xsl:template>
<!-- Template "type_ouverture" -->
<xsl:template match="type_ouverture">
 	<xsl:value-of select="."/>
 	<xsl:text>, </xsl:text>
</xsl:template>

<!-- Template "plage_j" -->
<xsl:template match="plage_j">

 	<xsl:choose>
	 	<xsl:when test="@j_debut_num != @j_fin_num">
	 	<xsl:text>du </xsl:text>
	 	<xsl:value-of select="@j_debut"/>
	 	<xsl:text> au </xsl:text>
	 	<xsl:value-of select="@j_fin"/>
 	</xsl:when>
 	<xsl:otherwise>
 	    <xsl:text>le </xsl:text>
 		<xsl:value-of select="@j_debut"/>
 	</xsl:otherwise>
 	</xsl:choose>
 	<xsl:apply-templates select="plage_h"/>
 	<br/>
</xsl:template>

<!-- Template "plage_h" -->
<xsl:template match="plage_h[position() = 1]">
 	<xsl:text> de </xsl:text>
 	<xsl:value-of select="@h_debut"/><xsl:text>h</xsl:text>
 	<xsl:if test="string-length(@min_debut)=1"><xsl:text>0</xsl:text></xsl:if>
 	<xsl:value-of select="@min_debut"/>
	 <xsl:text> à </xsl:text>
 	<xsl:value-of select="@h_fin"/><xsl:text>h</xsl:text>
 	<xsl:if test="string-length(@min_fin)=1"><xsl:text>0</xsl:text></xsl:if>
 	<xsl:value-of select="@min_fin"/>
</xsl:template>
<xsl:template match="plage_h[position() > 1]">
 	<xsl:text> et de </xsl:text>
 	<xsl:value-of select="@h_debut"/><xsl:text>h</xsl:text>
 	<xsl:if test="string-length(@min_debut)=1"><xsl:text>0</xsl:text></xsl:if>
 	<xsl:value-of select="@min_debut"/>
	 <xsl:text> à </xsl:text>
 	<xsl:value-of select="@h_fin"/><xsl:text>h</xsl:text>
 	<xsl:if test="string-length(@min_fin)=1"><xsl:text>0</xsl:text></xsl:if>
 	<xsl:value-of select="@min_fin"/>
</xsl:template>


<!-- Template "details_competence" -->
<xsl:template match="details_competence">

	<!--<br/><b><xsl:text>Compétences </xsl:text></b>-->

	<xsl:apply-templates select="info_locale"/>
	<xsl:apply-templates select="page_locale"/>
	<xsl:apply-templates select="complement_info"/>
	<xsl:apply-templates select="cout"/>
	<xsl:apply-templates select="delai"/>
	<xsl:apply-templates select="contact"/>

	<!--<br/><br/>-->

</xsl:template>

<!-- Template "complement_info" -->
<xsl:template match="complement_info">
 	<br/><xsl:value-of select="."/>
</xsl:template>
<!-- Template "cout" -->
<xsl:template match="cout">
 	<br/>Coût : <xsl:value-of select="."/>
</xsl:template>
<!-- Template "delai" -->
<xsl:template match="delai">
 	<br/>Délai : <xsl:value-of select="@quantite"/><xsl:text> </xsl:text><xsl:value-of select="@unite"/>
</xsl:template>
<!-- Template "contact" -->
<xsl:template match="contact">
 	<br/>Contact : <xsl:value-of select="."/>
</xsl:template>
<!-- Template "page_locale" -->
<xsl:template match="page_locale">
	<br/><a target="_blank" href="{url}"><xsl:value-of select="titre_url"/></a>
	<xsl:apply-templates select="description_url"/>
</xsl:template>
<xsl:template match="description_url[ancestor::page_locale]">
	<xsl:text> </xsl:text><xsl:value-of select="."/>
</xsl:template>

<!-- Template "info_locale" -->
<xsl:template match="info_locale">
	<p>
	<xsl:apply-templates select="titre_info"/>
	<xsl:apply-templates select="contenu_info"/>
	</p>
</xsl:template>

<xsl:template match="titre_info">

	<xsl:if test="current() and normalize-space(current())">
 	<xsl:copy-of select="$puce" />
 	<b>
 	<xsl:call-template name="substitute_retour_ligne">
		<xsl:with-param name="chaine" select="."/>
	</xsl:call-template>
	</b>
	</xsl:if>
</xsl:template>

<xsl:template match="contenu_info">
 	<p>
 	<xsl:call-template name="substitute_retour_ligne">
		<xsl:with-param name="chaine" select="."/>
	</xsl:call-template>
	</p>
</xsl:template>



<!-- ************************* -->
<!-- Template "numero_d_appel" -->
<xsl:template match="numero_d_appel">
	<xsl:apply-templates/>
</xsl:template>

<!-- Template "description_numero" -->
<xsl:template match="description_numero">
	<br/><xsl:value-of select="."/>
</xsl:template>
<!-- Template "cout_numero" -->
<xsl:template match="cout_numero">
	<xsl:apply-templates select="cout"/>
	<xsl:apply-templates select="type_tarification"/>
</xsl:template>

<!-- Template "type_tarification" -->
<xsl:template match="type_tarification">
	<br/>Tarification : <xsl:value-of select="."/>
</xsl:template>








<!-- ************************************************************************ -->
<!-- Template "infos_techniques" -->
<xsl:template match="/fiche_localisee/infos_techniques">
</xsl:template>


<!-- ************************************************************************ -->
<!-- Template "infos_reloc" -->
<xsl:template match="/fiche_localisee/infos_reloc">
</xsl:template>









<!-- ************************************************************************ -->
<!-- Template "substitute" -->
<!-- Permet de remplacer les /n d'une string par des BR -->
<xsl:template name="substitute_retour_ligne">
 <xsl:param name="chaine"/>
 <xsl:param name="changer" select="'\n'" />

 <xsl:choose>
   <xsl:when test="contains($chaine,$changer )">

	   <xsl:variable name="avant" select="substring-before($chaine,$changer)"/>
	    <xsl:variable name="apres" select="substring-after($chaine, $changer)"/>
	    <xsl:value-of select="$avant"/>
	    <br/>
	    <xsl:choose>
	      <xsl:when test="contains($apres,$changer)">
	      	<xsl:call-template name="substitute_retour_ligne">
	       	<xsl:with-param name="chaine" select="$apres"/>
	       	<xsl:with-param name="changer" select="$changer"/>
	      	</xsl:call-template>
	      </xsl:when>
	      <xsl:otherwise>
	      	<xsl:value-of select="$apres"/>
	      </xsl:otherwise>
	    </xsl:choose>

   </xsl:when>
   <xsl:otherwise>

   	<xsl:value-of select="$chaine"/>

   </xsl:otherwise>
 </xsl:choose>

</xsl:template>


<!-- ************************************************************************ -->
<!-- Template "format_number" -->
<!-- Permet d'ajouter des espaces dans un numéro de téléphone -->

<xsl:template name="format_number">
 <xsl:param name="chaine"/>
 <xsl:param name="ajouter" select="' '" />

 <xsl:choose>
   <xsl:when test="string-length($chaine)>2">

	   <xsl:variable name="avant" select="substring($chaine,1,2)"/>
	    <xsl:variable name="apres" select="substring($chaine, 3)"/>
	    <xsl:value-of select="$avant"/>
	    <xsl:value-of select="$ajouter"/>

	    <xsl:choose>
	      <xsl:when test="string-length($apres)>2">
	      	<xsl:call-template name="format_number">
	       	<xsl:with-param name="chaine" select="$apres"/>
	       	<xsl:with-param name="ajouter" select="$ajouter"/>
	      	</xsl:call-template>
	      </xsl:when>
	       <xsl:when test="string-length($apres)=2">
	      	<xsl:value-of select="$apres"/>
	      </xsl:when>
	      <xsl:when test="string-length($apres)=1">
	      	<xsl:value-of select="$apres"/>
	      </xsl:when>
	    </xsl:choose>

   </xsl:when>
   <xsl:otherwise>
   	<xsl:value-of select="$chaine"/>
   </xsl:otherwise>
 </xsl:choose>

</xsl:template>


</xsl:stylesheet>