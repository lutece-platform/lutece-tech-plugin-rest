<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<!-- le nom des styles à utiliser -->
<!-- les styles correpondants sont dans la feuille css "customize.css" -->
<xsl:variable name="tableStyle" select="'comarquage-table'" />
<xsl:variable name="tableRowOddStyle" select="'comarquage-table-odd-row'" />
<xsl:variable name="tableRowEvenStyle" select="'comarquage-table-even-row'" />
<xsl:variable name="tableHeaderStyle" select="'comarquage-table-header'" />
<xsl:variable name="tableFooterStyle" select="'comarquage-table-footer'" />

<xsl:variable name="highlightStyle" select="'comarquage-highlight'" />
<xsl:variable name="amountStyle" select="'comarquage-amount'" />
<xsl:variable name="quotationStyle" select="'comarquage-quotation'" />


<!-- ************************************************************************ -->
<!-- Formatage de fiche, chapitre, paragraphe, ...  -->

<!-- *** Le template fiche *** -->
<xsl:template match="fiche">
	<h1 id="Debut{@id}"><xsl:value-of select="titre"/></h1>
	<xsl:apply-templates select="Chapitre"/>
	<br/>
</xsl:template>


<!-- *** Le template Texte *** -->
<xsl:template match="Texte">
	<xsl:apply-templates />
</xsl:template>


<!-- *** Le template Chapitre *** -->
<xsl:template match="Chapitre">
	<xsl:apply-templates/>
</xsl:template>


<!-- *** Le template Titre *** -->
<xsl:template match="Titre">

	<xsl:if test="name(parent::node())='Chapitre'">
		<h3>
		<xsl:copy-of select="$puce" />
		<xsl:apply-templates select="Paragraphe" mode="NotFormatted"/>
		</h3>
	</xsl:if>

	<xsl:if test="name(parent::node())='SousChapitre'">
		<h3><xsl:apply-templates select="Paragraphe" mode="NotFormatted"/></h3>
	</xsl:if>

</xsl:template>



<!-- *** Le template SousChapitre *** -->
<xsl:template match="SousChapitre">
	<xsl:apply-templates/>
</xsl:template>


<!-- *** Le template Liste *** -->
<xsl:template match="Liste">
	<ul>
	 <xsl:apply-templates select="Item"/>
	</ul>
</xsl:template>



<!-- *** Le template Item *** -->
<xsl:template match="Item">
	<li>
	 <xsl:apply-templates select="Paragraphe" mode="NotFormatted"/>
	 <xsl:apply-templates select="Liste"/>
	</li>
</xsl:template>



<!-- *** Le template Tableau *** -->
<xsl:template match="Tableau">
	<center>
	<table class="{$tableStyle}">
	 <xsl:apply-templates select="Rangée"/>
	</table>
	</center>
</xsl:template>



<!-- *** Le template Rangée *** -->
<xsl:template match="Rangée">
	<xsl:choose>

		<xsl:when test="@type='header'">
			<tr class="{$tableHeaderStyle}">

				<xsl:apply-templates select="Cellule"/>
			</tr>
		</xsl:when>


		<xsl:when test="@type='footer'">
			<tr class="{$tableFooterStyle}">

				<xsl:apply-templates select="Cellule"/>
			</tr>
		</xsl:when>

		<xsl:otherwise>
			<xsl:choose>
				<xsl:when test="position() mod 2 = 0">
					<tr class="{$tableRowEvenStyle}">
					 <xsl:apply-templates select="Cellule"/>
					</tr>
				</xsl:when>
				<xsl:otherwise>
					<tr class="{$tableRowOddStyle}">
					<xsl:apply-templates select="Cellule"/>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:otherwise>


	</xsl:choose>
</xsl:template>



<!-- *** Le template Cellule *** -->
<xsl:template match="Cellule">
	<td>
		<xsl:apply-templates select="Paragraphe" mode="NotFormatted"/>
	</td>
</xsl:template>



<!-- ************************************************************************ -->

<!-- Pour un paragraphe à formatter (chapitre, sous-chapitre) -->
<xsl:template match="Paragraphe">
	<p align="justify"><xsl:apply-templates /></p>
</xsl:template>


<!-- Pour les listes, cellules, titres ... -->
<xsl:template match="Paragraphe" mode="NotFormatted" >
	<xsl:apply-templates />
</xsl:template>




<!-- ************************************************************************ -->
<!-- formatages de paragraphes -->

<xsl:template match="MiseEnEvidence">
	<span class="{$highlightStyle}"><xsl:value-of select="."/></span>
</xsl:template>

<xsl:template match="LienExterne">
	<a target="_blank" href="{@URL}"><xsl:value-of select="."/></a>
</xsl:template>

<xsl:template match="Citation">
	<span class="{$quotationStyle}"><xsl:value-of select="."/></span>
</xsl:template>

<xsl:template match="Montant">
	<span class="{$amountStyle}">
		<xsl:value-of select="."/>
		<xsl:text> EUR</xsl:text>
	</span>
</xsl:template>


<xsl:template match="LienInterne">
	<a href="#{@LienPublication}"><xsl:value-of select="."/></a>
</xsl:template>


</xsl:stylesheet>