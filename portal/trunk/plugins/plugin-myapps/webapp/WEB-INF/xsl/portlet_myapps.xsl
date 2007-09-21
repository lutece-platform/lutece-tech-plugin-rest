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
                <strong></strong>	<xsl:apply-templates select="myapps-list" />
            </div>
        </div>
    </xsl:template>
    
    
    <xsl:template match="myapps-list">
        <table cellpadding="0" cellspacing="0"  width="100%">
            <tr>
                <td>
                    <table>
                        <xsl:apply-templates select="myapp" />
                        <xsl:value-of select="message" />
                    </table>
                </td>
            </tr>
            <tr>
                <td class="myapps-portlet">
                    <form action="" name="XPage" target="_top">
                        <div>
                            <input type="hidden" name="page" value="myapps" />
                            <xsl:text disable-output-escaping="yes"><![CDATA[<input type="submit" value="]]></xsl:text><xsl:value-of disable-output-escaping="yes" select="myapp-button" />
                            <xsl:text disable-output-escaping="yes"><![CDATA["/>]]></xsl:text>
                        </div>
                    </form>
                </td>
            </tr>
        </table>
    </xsl:template>
    
    <xsl:template match="myapp" >
        <tr>
            <td>
                <img src="{myapp-icon}" width="32" height="32" alt="{myapp-description}"/>
            </td>
            <td>
                <strong>
                    <a href="{myapp-link}&amp;plugin_name=myapps" target="_blank">
                        <xsl:value-of select="myapp-name" />
                    </a>
                </strong>
                <br />
                <small>
                    <xsl:value-of select="myapp-description" />
                </small>
            </td>
        </tr>
    </xsl:template>
    
</xsl:stylesheet>