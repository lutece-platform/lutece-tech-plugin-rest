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
<xsl:variable name="contents"/>

<xsl:template match="/">
	Debut indexer (path '<xsl:value-of select="$targetDirectory"/>', create directory <xsl:value-of select="$_createDirectory"/>): <xsl:value-of select="xalan://lucene:startIndexer($targetDirectory, $_createDirectory)"/>

	<xsl:value-of select="xalan://lucene:startDocument()"/>
		<xsl:variable name="id" select="substring-before(fiche_localisee/fiche_pratique/@url, '.xml')"/>
		<xsl:variable name="urlComplet">
			<xsl:value-of select="concat('jsp/site/Portal.jsp?page=', $pluginName, '&amp;','id=', 'N0/', $id)"/>
		</xsl:variable>

		<xsl:value-of select="xalan://lucene:addUnIndexed('uid', $id)"/>
		<xsl:value-of select="xalan://lucene:addUnIndexed('title', string(fiche_localisee/fiche_pratique/titre))"/>
		<xsl:value-of select="xalan://lucene:addUnIndexed('url', $urlComplet)"/>

		<xsl:variable name="contents">
			<xsl:for-each select="//text()">
				<xsl:value-of select="concat(., ' ')"/>
			</xsl:for-each>
		</xsl:variable>

		<xsl:value-of select="xalan://lucene:addUnStored('contents', $contents)"/>
	<xsl:value-of select="xalan://lucene:endDocument()"/>

	Fin et optimize indexer: <xsl:value-of select="xalan://lucene:endIndexer()"/>
</xsl:template>

</xsl:stylesheet>
