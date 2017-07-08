<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns="http://www.w3.org/1999/xhtml">

    <xsl:output method="xml"
                doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
                doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
                encoding="UTF-8"
                indent="yes"
    />

    <!--
     Please complete the stylesheet.
     For instructions, read the comments in XSLTProcesor.java
     If you need to change the root element or the xsl:output,
     you can do it.
    -->
    <xsl:template match="contacts">
        <!-- You will probably need to have this template completed -->

        <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <title>Contacts</title>
                <meta http-equiv="Content-Style-Type" content="text/css"/>
                <link rel="stylesheet" href="contacts.css" type="text/css" media="screen"/>
            </head>
            <body>
              <h1>
                  Contacts list
              </h1>

              <table>
                <tr>
                  <th>Name</th>
                  <th>Surame</th>
                </tr>
                <xsl:apply-templates select="contact"/>
              </table>
            </body>
        </html>

    </xsl:template>

    <xsl:template match="contact">

      <xsl:variable name='cid'>
          <xsl:value-of select='@cid'/>
      </xsl:variable>

      <tr class="contact" id="{$cid}">
        <td><xsl:value-of select="name"/></td>
        <td><xsl:value-of select="surname"/></td>
      </tr>
      <section id="{$cid}">
        <xsl:apply-templates select="info"/>
      </section>
    </xsl:template>

    <xsl:template match="info">
          <xsl:apply-templates select="address"/>
          <xsl:apply-templates select="emails"/>
          <xsl:apply-templates select="phones"/>
    </xsl:template>

    <xsl:template match="address">
      <p><xsl:value-of select="street"/></p>
      <p><xsl:value-of select="number"/></p>
      <p><xsl:value-of select="city"/></p>
    </xsl:template>

    <xsl:template match="emails">
      <h3>Emails:</h3>
      <ul>
      <xsl:for-each select="email">
        <li><xsl:value-of select="."/></li>
      </xsl:for-each>
      </ul>
    </xsl:template>

    <xsl:template match="phones">
      <h3>Phones:</h3>
      <ul>
      <xsl:for-each select="phone">
        <li><xsl:value-of select="."/></li>
      </xsl:for-each>
      </ul>
    </xsl:template>
</xsl:stylesheet>
