<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:param name="site-path" select="site-path" />
<xsl:variable name="portlet-id" select="portlet/portlet-id" />
<xsl:variable name="e-mail-error" select="portlet/newsletter-email-error" />
<xsl:variable name="nochoice-error" select="portlet/subscription-nochoice-error" />
  <xsl:template match="portlet">
    <div class="portlet-background-colored" >
      <xsl:if test="not(string(display-portlet-title)='1')">
        <h3 class="portlet-background-colored-header">
          <xsl:value-of disable-output-escaping="yes" select="portlet-name" />
        </h3>
      </xsl:if>
      <div class="portlet-background-colored-content" >
<form class="default-form" id="newsletter" action="jsp/site/plugins/newsletter/DoSubscribePortletNewsLetter.jsp" method="post" >
        <xsl:apply-templates select="newsletter-subscription-list" />
	<div>

<input name="plugin_name" value="newsletter" type="hidden" />
		<label for="email"><xsl:value-of select="newsletter-subscription-email" /></label>

    <xsl:choose>
        <xsl:when test="not($e-mail-error>'1')">
		      <input name="email" id="email" size="40" maxlength="100" type="text" />
		
		        <div>
			           <xsl:value-of select="$e-mail-error" />	
			</div>
		<xsl:choose>
			<xsl:when test="not($nochoice-error>'1')">
				<div>
					<xsl:value-of select="$nochoice-error" />	
				</div>
			</xsl:when>
		</xsl:choose>
		
	</xsl:when>

		<xsl:otherwise>
 <input name="email" id="email" size="40" maxlength="100" type="text" />
		</xsl:otherwise>
      </xsl:choose>
		
		 

  <xsl:text disable-output-escaping="yes"><![CDATA[<input class="button" value="]]></xsl:text><xsl:value-of select="newsletter-subscription-button" /> <xsl:text disable-output-escaping="yes"><![CDATA[" type="submit" />]]></xsl:text>
 <xsl:text disable-output-escaping="yes"><![CDATA[<input name="portlet_id" value="]]></xsl:text><xsl:value-of select="portlet-id" /><xsl:text disable-output-escaping="yes"><![CDATA[" type="hidden"/>]]></xsl:text>
</div>
</form>
      </div>

    </div>
  </xsl:template>


  <xsl:template match="newsletter-subscription-list">
    <ul>
      <xsl:apply-templates select="newsletter-subscription" />
    </ul>
  </xsl:template>


  <xsl:template match="newsletter-subscription">
    <li>
 <xsl:text disable-output-escaping="yes"><![CDATA[<input type="checkbox" class="checkbox-field" name="newsletter]]></xsl:text><xsl:value-of select="newsletter-subscription-id" /> <xsl:text disable-output-escaping="yes"><![CDATA[" id="newsletter]]></xsl:text><xsl:value-of select="newsletter-subscription-id" /><xsl:text disable-output-escaping="yes"><![CDATA[" checked="checked" />]]></xsl:text>

        <b><xsl:value-of select="newsletter-subscription-subject"/></b>
      <br />
    </li>
  </xsl:template>

</xsl:stylesheet>

