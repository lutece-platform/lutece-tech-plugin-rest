<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet
	version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"

	xmlns:axslt="http://xml.apache.org/xslt"
	xmlns:xalan="http://xml.apache.org/xalan"
	xmlns:lucene="fr.paris.lutece.plugins.comarquage.util.search.XslXalanExtension"
	exclude-result-prefixes="lucene"
>

<xsl:param name="targetDirectory">fichesCDC</xsl:param>
<xsl:param name="createDirectory">true</xsl:param>
<xsl:param name="pluginName"></xsl:param>

<xsl:variable name="_createDirectory" select="$createDirectory = 'true'"/>
<!-- 
<axslt:component prefix="lucene"
                 functions="startIndexer,startDocument,addText,addUnStored,addUnIndexed,addKeywords,endDocument,optimize,endIndexer">
	<axslt:script lang="javaclass"
	              src="fr.paris.lutece.plugins.comarquage.util.search.XslXalanExtension"/>
</axslt:component>
-->
<xsl:template match="/">

	Debut indexer (path '<xsl:value-of select="$targetDirectory"/>', create directory <xsl:value-of select="$_createDirectory"/>): <xsl:value-of select="xalan://lucene:startIndexer($targetDirectory, $_createDirectory)"/>

<!-- [position() &lt; 20] -->
	<xsl:for-each select="fiches/fiche">
		Indexe fiche: <xsl:value-of select="titre"/>
		<xsl:value-of select="xalan://lucene:startDocument()"/>

		<xsl:variable name="path" select="substring-after(url, 'f=')"/>
		<xsl:variable name="urlComplet">
			<xsl:value-of select="concat('jsp/site/Portal.jsp?page=', $pluginName, '&amp;','id=', $path)"/>
		</xsl:variable>

		<xsl:value-of select="xalan://lucene:addUnIndexed('uid', substring-after($path, '/'))"/>
		<xsl:value-of select="xalan://lucene:addUnIndexed('title', string(titre))"/>
		<xsl:value-of select="xalan://lucene:addUnIndexed('url', $urlComplet)"/>

		<!--xsl:value-of select="lucene:addKeywords('mot-cle', string(mots-cles), ',')"/-->
		<xsl:variable name="contents">
			<xsl:value-of select="concat(titre, ' ', mots-cles, ' ')"/>
			<xsl:for-each select="themes/theme">
				<xsl:value-of select="concat(., ' ')"/>
			</xsl:for-each>
		</xsl:variable>

		<xsl:value-of select="xalan://lucene:addUnStored('contents', $contents)"/>

		<xsl:value-of select="xalan://lucene:endDocument()"/>
	</xsl:for-each>

	Fin et optimize indexer: <xsl:value-of select="xalan://lucene:endIndexer()"/>
</xsl:template>

</xsl:stylesheet>
