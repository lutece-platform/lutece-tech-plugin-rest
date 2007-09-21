<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:param name="pluginName"></xsl:param>

<xsl:variable name="linkId" >
	jsp/site/Portal.jsp?page=<xsl:value-of select="$pluginName" />&amp;id
</xsl:variable>


<xsl:variable name="puce" >
	<img src="images/local/skin/bullet.gif" align="middle" /><xsl:text> </xsl:text>
</xsl:variable>


<xsl:template match="/">
 <div id="three-zones-first-and-second">

 	<div id="three-zones-first">
	<div id="three-zones-first-content">


				<p><xsl:copy-of select="$puce" />
				<a href="{$linkId}=N0/LN1">Citoyenneté</a>
				&amp;
				<a href="{$linkId}=N0/N22">Administration</a></p>

				<p><xsl:copy-of select="$puce" />
				<a href="{$linkId}=N0/N10/N127">Enfant</a>
				&amp;
				<a href="{$linkId}=N0/N10">Famille</a></p>

				<p><xsl:copy-of select="$puce" />
				<a href="{$linkId}=N0/N11">Formation</a>
				&amp;
				<a href="{$linkId}=N0/N5">Emploi</a></p>

				<p><xsl:copy-of select="$puce" />
				<a href="{$linkId}=N0/N12">Personnes handicapées</a></p>

				<p><xsl:copy-of select="$puce" />
				<a href="{$linkId}=N0/N15">Urbanisme - Logement</a></p>

				<p><xsl:copy-of select="$puce" />
				<a href="{$linkId}=N0/N21">Vos papiers</a></p>



	</div>

	</div>
	<div id="three-zones-second">
	<div id="three-zones-second-content">

				<p><xsl:copy-of select="$puce" />
				<a href="{$linkId}=N0/N18">Déplacements - Transports</a></p>

				<p><xsl:copy-of select="$puce" />
				<a href="{$linkId}=N0/N7">Environnement</a></p>

				<p><xsl:copy-of select="$puce" />
				<a href="{$linkId}=N0/LN2">Jeunesse - Sport</a></p>

				<p><xsl:copy-of select="$puce" />
				<a href="{$linkId}=N0/N16">Retraite</a></p>

				<p><xsl:copy-of select="$puce" />
				<a href="{$linkId}=N0/N19">Vacances - Loisirs</a></p>

	</div>
	</div>
</div>


<div id="three-zones-third">
<div id="three-zones-third-content">


				<p><xsl:copy-of select="$puce" />
				<a href="{$linkId}=N0/N6">Education</a>
				&amp;
				<a href="{$linkId}=N0/LN3">Culture</a></p>

				<p><xsl:copy-of select="$puce" />
				<a href="{$linkId}=N0/LN4">Fiscalité - Consommation</a></p>

				<p><xsl:copy-of select="$puce" />
				<a href="{$linkId}=N0/N14">Justice</a></p>

				<p><xsl:copy-of select="$puce" />
				<a href="{$linkId}=N0/N14">Santé - Action sociale</a></p>

				<p><xsl:copy-of select="$puce" />
				<a href="{$linkId}=N0/N20">Vie associative</a></p>


</div>
</div>


</xsl:template>


</xsl:stylesheet>