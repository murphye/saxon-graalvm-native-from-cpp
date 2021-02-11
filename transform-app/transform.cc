#include <iostream>
#include "libsaxon-native.h"
#include <chrono>
using namespace std;
using namespace std::chrono;

int main() {
    auto start = high_resolution_clock::now();

    graal_isolate_t *isolate = NULL;
    graal_isolatethread_t *thread = NULL;

    if (graal_create_isolate(NULL, &isolate, &thread) != 0) {
        fprintf(stderr, "initialization error\n");
        return 1;
    }

    const char* xml = R"(<G_1><ORGANIZATION_NAME>My Company 1</ORGANIZATION_NAME><ORGANIZATIONID>901</ORGANIZATIONID><ITEMNUMBER>20001</ITEMNUMBER><ITEMDESCRIPTION>Item Description 1</ITEMDESCRIPTION><ORGANIZATION_NAME>My Company 1</ORGANIZATION_NAME><ORGANIZATIONID>901</ORGANIZATIONID><ITEMNUMBER>20002</ITEMNUMBER><ITEMDESCRIPTION>Item Description 2</ITEMDESCRIPTION><ORGANIZATION_NAME>My Company 1</ORGANIZATION_NAME><ORGANIZATIONID>901</ORGANIZATIONID><ITEMNUMBER>20003</ITEMNUMBER><ITEMDESCRIPTION>Item Description 3</ITEMDESCRIPTION></G_1>)";
    const char* xslt = R"(<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/2005/xpath-functions"><xsl:output method="text" encoding="UTF-8"/><xsl:template match="/G_1"><xsl:variable name="xml"><array> <xsl:for-each-group select="*" group-starting-with="ORGANIZATION_NAME"><map><string key="Item_Number"><xsl:value-of select="current-group()[self::ITEMNUMBER]"/></string><string key="Item_Description"><xsl:value-of select="current-group()[self::ITEMDESCRIPTION]"/></string></map></xsl:for-each-group></array></xsl:variable><xsl:value-of select="xml-to-json($xml) "/></xsl:template></xsl:stylesheet>)";

    printf("Result> %s\n",transform(thread, (char*)xml, (char*)xslt));

    auto stop = high_resolution_clock::now();
    auto duration = duration_cast<microseconds>(stop - start);
    cout << duration.count() << endl;

    return 0;
}