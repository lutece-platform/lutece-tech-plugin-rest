<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:param name="site-path" select="site-path" />
<xsl:variable name="portlet-id" select="portlet/portlet-id" />


<xsl:template match="portlet">
	<div class="portlet-background-colored" id="portlet_id_{portlet-id}">
        <xsl:if test="not(string(display-portlet-title)='1')">
			<h3 class="portlet-background-colored-header">
				<xsl:value-of disable-output-escaping="yes" select="portlet-name" />
			</h3>
        </xsl:if>

		<div class="portlet-background-colored-content">
			<xsl:apply-templates select="whatsnew-list-portlet" />
		</div>
	</div>
</xsl:template>


<xsl:template match="whatsnew-list-portlet">
	
	<xsl:choose>
		<xsl:when test="count(whatsnew-element) = 0">
			<i>Aucun &#233;l&#233;ment &#224; afficher</i> 
		</xsl:when>
		<xsl:otherwise>

			<!-- min element index to display -->
			<xsl:variable name="min" select="whatsnew-min-display" />
			<!-- max element index to display -->
			<xsl:variable name="max" select="whatsnew-min-display + whatsnew-number-display - 1" />

			<!-- request parameter name corresponding to the min element index to display -->
			<xsl:variable name="min_display_param"  >
			     <xsl:text>min_display_</xsl:text><xsl:value-of select="../portlet-id" />
			</xsl:variable>

			<!-- list of all elements -->
			<ul>
			    <xsl:apply-templates select="whatsnew-element" />
			</ul>

			<div align="right">
			<!-- display button "previous", only if first element displayed is not the first of the list -->
			<xsl:if test="$min  &#62; 1" > 

				<xsl:variable name="min-previous" select="$min - whatsnew-number-display" />
				<a href="{$site-path}?page_id={../page-id}&#38;{$min_display_param}={$min-previous}#portlet_id_{../portlet-id}" target="_top">[Pr&#233;c&#233;dents]</a>

			</xsl:if>

			<!-- display the indexes of the currently displayed elements, and also give the total number of elements -->
			
			<xsl:choose>
				<xsl:when test="$max  &#60; count(whatsnew-element)" > 
					
					<xsl:choose>
					  	<xsl:when test="$min = $max">
					  		[<xsl:value-of select="$min" /> / <xsl:value-of select="count(whatsnew-element)" />]
					  	</xsl:when>
					  	<xsl:otherwise>
					  		[<xsl:value-of select="$min" />-<xsl:value-of select="$max" /> / <xsl:value-of select="count(whatsnew-element)" />]
					  	</xsl:otherwise>
					</xsl:choose>
					
				</xsl:when>
				<xsl:when test="$max  &gt; count(whatsnew-element)" > 
					
					<xsl:choose>
						<xsl:when test="$min = count(whatsnew-element)">
					  		[<xsl:value-of select="$min" /> / <xsl:value-of select="count(whatsnew-element)" />]
					  	</xsl:when>
					  	<xsl:otherwise>
					  		[<xsl:value-of select="$min" />-<xsl:value-of select="count(whatsnew-element)" /> / <xsl:value-of select="count(whatsnew-element)" />]
					  	</xsl:otherwise>
					</xsl:choose>	
					
				</xsl:when>
				<xsl:when test="$max  = count(whatsnew-element)" > 
					
					<xsl:choose>
						<xsl:when test="$min = $max">
							[<xsl:value-of select="$min" /> / <xsl:value-of select="count(whatsnew-element)" />]
						</xsl:when>
						<xsl:otherwise>
							[<xsl:value-of select="$min" />-<xsl:value-of select="$max" /> / <xsl:value-of select="count(whatsnew-element)" />]
						</xsl:otherwise>
					</xsl:choose>
					
				</xsl:when>
			</xsl:choose>
			

			<!-- display button "next", only if last element displayed is not the last of the list -->
			<xsl:if test="$max  &#60; count(whatsnew-element)" > 
				<xsl:variable name="min-next" select="$max + 1" />
				<a href="{$site-path}?page_id={../page-id}&#38;{$min_display_param}={$min-next}#portlet_id_{../portlet-id}" target="_top">[Suivants]</a>
			</xsl:if>
			</div>
			
		
		 </xsl:otherwise>
	</xsl:choose>
	
</xsl:template>

<xsl:template match="whatsnew-element">
  

	 <xsl:if test="((position() &#62; ../whatsnew-min-display) or (position() = ../whatsnew-min-display)) and (position() &#60; (../whatsnew-min-display+../whatsnew-number-display)) " >
  
		<li>
			<b><a href="{$site-path}{whatsnew-url}" target="_top"><xsl:value-of select="whatsnew-title" /></a></b><br/>
			<i><xsl:value-of select="whatsnew-type" /></i><br/>
			<xsl:apply-templates select="whatsnew-description" />
			<xsl:value-of select="whatsnew-date-update" /><br/><br/>
		</li>

	</xsl:if>

</xsl:template>

<xsl:template match="whatsnew-description">
	<xsl:value-of select="." /><br/>
</xsl:template>


</xsl:stylesheet>
