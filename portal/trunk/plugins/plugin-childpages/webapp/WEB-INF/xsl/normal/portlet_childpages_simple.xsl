<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:param name="site-path" select="site-path" />

<xsl:template match="portlet">
	<div class="portlet-background-colored">
        <xsl:if test="not(string(display-portlet-title)='1')">
			<h3 class="portlet-header">
				<xsl:value-of disable-output-escaping="yes" select="portlet-name" />
			</h3>
        </xsl:if>
		<div class="portlet-content">
			<xsl:apply-templates select="child-pages-portlet" />
		</div>
	</div>
</xsl:template>

<xsl:template match="child-pages-portlet">
	<ul>
	    <xsl:apply-templates select="child-page" />
	</ul>
</xsl:template>

<xsl:template match="child-page">
	<li>
		<a href="{$site-path}?page_id={child-page-id}" target="_top">
			<b><xsl:value-of select="child-page-name" /></b>
		</a><br/>
		<xsl:value-of select="child-page-description" /><br/>
	</li>
</xsl:template>

</xsl:stylesheet>