<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="current-space-id" select="current-space-id" />


    <xsl:variable name="current-space">
        <xsl:value-of select="current-space-id" />
    </xsl:variable>

    <xsl:template match="spaces">
        <ul>
            <xsl:apply-templates select="space" />
        </ul>
    </xsl:template>
    
    <xsl:template match="space">
        <li>
            <img src="{space-icon-url}" alt="" hspace="5" />
            <a href="jsp/admin/plugins/document/ManageDocuments.jsp?id_space_filter={space-id}">
                <xsl:choose>
                    <xsl:when test="space-id=$current-space-id" >
                        <strong><xsl:value-of select="name" /></strong>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="name" />
                    </xsl:otherwise>
                </xsl:choose>
            </a>
            <xsl:apply-templates select="child-spaces" />
        </li>  
    </xsl:template>

    <xsl:template match="child-spaces">
        <ul>
            <xsl:apply-templates select="space" />
        </ul>
    </xsl:template>

</xsl:stylesheet>
