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
			    <xsl:text disable-output-escaping="yes">
		    	    <![CDATA[<div class="clear">&#160;</div>]]>
		            </xsl:text>
		</div>
	</div>
</xsl:template>


<xsl:template match="child-pages-portlet">
	<xsl:apply-templates select="child-page" />
</xsl:template>


<xsl:template match="child-page">
	<xsl:if test="position() mod 4 =1" >
		<xsl:if test="position() = 1" >
        	<xsl:text disable-output-escaping="yes"><![CDATA[<div class="line-first">]]></xsl:text>
        </xsl:if>
        <xsl:if test="position() > 1" >
        	<xsl:text disable-output-escaping="yes"><![CDATA[<div class="line">]]></xsl:text>
        </xsl:if>
    </xsl:if>

	<div class="line-element">
		<p>
	    	<xsl:apply-templates select="child-page-image" />
 				<a href="{$site-path}?page_id={child-page-id}" target="_top">
				<strong><xsl:value-of select="child-page-name" /></strong>
			</a><br />
			<xsl:value-of select="child-page-description" />
		</p>
	</div>

    <xsl:if test="(position() mod 4 =0) or (position()=last())" >
	    <xsl:text disable-output-escaping="yes"><![CDATA[</div>]]></xsl:text>
    </xsl:if>

    <xsl:if test="position()=last()" >
	    <xsl:text disable-output-escaping="yes">
    	    <![CDATA[<div class="clear">&#160;</div>]]>
        </xsl:text>
    </xsl:if>
</xsl:template>


<!-- Format image polaroide -->
<xsl:template match="child-page-image">
    <div class="polaroid" >
       <img border="0" width="80" height="80" src="{.}" alt="" />
    </div>
</xsl:template>


</xsl:stylesheet>