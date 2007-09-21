<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:template match="portlet">
	<div class="portlet">
        <xsl:if test="not(string(display-portlet-title)='1')">
			<h3 class="portlet-header">
				<xsl:value-of disable-output-escaping="yes" select="portlet-name" />
			</h3>
        </xsl:if>

		<div class="portlet-content">
			<xsl:apply-templates select="links-portlet" />
		</div>
	</div>
</xsl:template>


<xsl:template match="links-portlet">
	 <xsl:choose>
	    <xsl:when test="count(link) != 0"> 
			
			<table>
				<xsl:apply-templates select="link" />
			</table>
	
		</xsl:when>
	    <xsl:otherwise>&#160;</xsl:otherwise>
	</xsl:choose>
</xsl:template>


<xsl:template match="link">
    <tr>
        <td rowspan="2" width="10"></td>
        <td rowspan="2"><a href="{link-url}" target="_blank">
			<xsl:apply-templates select="link-image" /></a>
        </td>
        <td>
			<a href="{link-url}" target="_blank">
		        <xsl:apply-templates select="link-name" />
			</a>
        </td>
    </tr>
    <tr>
        <td>
           <xsl:if test="normalize-space(link-description)" >
               <small>
                   <xsl:value-of select="link-description" />
               </small>
           </xsl:if>
        </td>
    </tr>
    <tr>
        <td colspan="2" height="5">&#160;</td>
    </tr>
</xsl:template>


<xsl:template match="link-image">
    <img src="{.}" border="0" alt="{link-url}" title="{link-url}" width="60"/>
</xsl:template>

</xsl:stylesheet>
