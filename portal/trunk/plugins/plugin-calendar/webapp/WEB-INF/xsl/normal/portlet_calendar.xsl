<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:param name="site-path" select="site-path" />
    <xsl:variable name="portlet-id" select="portlet/portlet-id" />
    <xsl:template match="portlet">
        <div class="portlet-background-colored" >
            <xsl:if test="not(string(display-portlet-title)='1')">
                <h3 class="portlet-background-colored-header">
                    <xsl:value-of disable-output-escaping="yes" select="portlet-name" />
                </h3>
            </xsl:if>
            <div class="portlet-background-colored-content" >
                <xsl:apply-templates select="calendar-filtered-list" />
            </div>
        </div>
    </xsl:template>
    <xsl:template match="calendar-filtered-list">
        <xsl:apply-templates select="events" />
    </xsl:template>
    <xsl:template match="events">    
        <xsl:apply-templates select="event" />  
    </xsl:template>
    <xsl:template match="event">
        [
        <xsl:value-of select="date-local" /> 
        ]
        <a href="{$site-path}?page=calendar&#38;agenda={agenda-id}&#38;date={event-date}" target="_top" title=" {agenda-name}:{event-title} ">
            
            <xsl:value-of select="event-title" /> 
        </a>
        <br />
    </xsl:template>
</xsl:stylesheet>