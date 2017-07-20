<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns="http://www.w3.org/1999/xhtml">

    <xsl:output method="xml"
                doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
                doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
                encoding="UTF-8"
                indent="yes"
                omit-xml-declaration="yes"
    />

    <xsl:template match="contacts">
        <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
              <meta charset="utf-8" />
              <title>Contacts</title>
              <style rel="stylesheet" type="text/css">
                .table div {
                    width: 50%;
                    float: left;
                }

                .contactInfo div {
                    width: 50%;
                    float: right;
                }

                .contact {
                    transition: all .5s ease;
                }

                tr th {
                    font-size: 22px;
                    min-width: 100px;
                }

                .contact td {
                    border: 1px solid #3c678d;
                    padding: 3px 5px;
                }
              </style>
              <script src="https://code.jquery.com/jquery-3.2.1.min.js"
                integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
                crossorigin="anonymous">//</script>
              <script type="text/javascript">
                $(document).ready(function () {
                    $('.info').hide();

                    $('.contact').click(function () {
                        $('.contact').css('color', 'black');
                        $('.contact').css('background', 'none');
                        $('.info').hide();

                        var cid = ($(this).attr("class").split(' ')[1]);
                        $(this).css('color', 'white');
                        $(this).css('background', '#3c678d');
                        $('.' + cid).show();
                    });
                });
              </script>
            </head>
            <body>
              <h1 id="title">Contact Book</h1>
              <div id="table" style="float:left; width:50%">
                <table>
                  <tr>
                    <th>Name</th>
                    <th>Surname</th>
                  </tr>
                  <xsl:apply-templates select="contact"/>
                </table>
              </div>
              <div id="contactInfo" style="float:right; width:50%">
                <xsl:apply-templates select="contact" mode="info"/>
              </div>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="contact">
      <xsl:variable name='cid'>
          <xsl:value-of select='@cid'/>
      </xsl:variable>
      <tr class="contact {$cid}">
        <td class="name"><xsl:value-of select="name"/></td>
        <td class="surname"><xsl:value-of select="surname"/></td>
      </tr>
    </xsl:template>

    <xsl:template match="contact" mode="info">
      <xsl:apply-templates select="info"/>
    </xsl:template>

    <xsl:template match="info">
      <xsl:variable name='iid'>
          <xsl:value-of select='../@cid'/>
      </xsl:variable>
      <div class="info {$iid}">
          <h2 class="name">
            <xsl:value-of select="../name"/>
            <xsl:text>&#xA0;</xsl:text>
            <xsl:value-of select="../surname"/>
          </h2>
          <xsl:apply-templates select="address"/>
          <xsl:apply-templates select="emails"/>
          <xsl:apply-templates select="phones"/>
      </div>
    </xsl:template>

    <xsl:template match="address">
      <h3>Address</h3>
      <p><xsl:value-of select="street"/></p>
      <p><xsl:value-of select="number"/></p>
      <p><xsl:value-of select="city"/></p>
    </xsl:template>

    <xsl:template match="emails">
      <h3>Emails</h3>
      <ul class="emails">
        <xsl:for-each select="email">
          <li><xsl:value-of select="."/></li>
        </xsl:for-each>
      </ul>
    </xsl:template>

    <xsl:template match="phones">
      <h3>Phones</h3>
      <ul class="phones">
        <xsl:for-each select="phone">
          <li><xsl:value-of select="."/></li>
        </xsl:for-each>
      </ul>
    </xsl:template>
</xsl:stylesheet>
