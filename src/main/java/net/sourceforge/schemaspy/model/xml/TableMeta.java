/*
 * This file is a part of the SchemaSpy project (http://schemaspy.sourceforge.net).
 * Copyright (C) 2004, 2005, 2006, 2007, 2008, 2009, 2010 John Currier
 *
 * SchemaSpy is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * SchemaSpy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.sourceforge.schemaspy.model.xml;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;


/**
 * Additional metadata about a table as expressed in XML instead of from
 * the database.
 *
 * @author John Currier
 */
@Slf4j
public class TableMeta {
    private final String name;
    private final String comments;
    private final List<TableColumnMeta> columns = new ArrayList<TableColumnMeta>();
    private final String remoteSchema;

    TableMeta(Node tableNode) {
        NamedNodeMap attribs = tableNode.getAttributes();

        name = attribs.getNamedItem("name").getNodeValue();

        Node commentNode = attribs.getNamedItem("comments");
        if (commentNode != null) {
            String tmp = commentNode.getNodeValue().trim();
            comments = tmp.length() == 0 ? null : tmp;
        } else {
            comments = null;
        }

        Node remoteSchemaNode = attribs.getNamedItem("remoteSchema");
        if (remoteSchemaNode != null) {
            remoteSchema = remoteSchemaNode.getNodeValue().trim();
        } else {
            remoteSchema = null;
        }

        logger.debug("Found XML table metadata for " + name +
                " remoteSchema: " + remoteSchema +
                " comments: " + comments);

        NodeList columnNodes = ((Element) tableNode.getChildNodes()).getElementsByTagName("column");

        for (int i = 0; i < columnNodes.getLength(); ++i) {
            Node colNode = columnNodes.item(i);
            columns.add(new TableColumnMeta(colNode));
        }
    }

    public String getName() {
        return name;
    }

    public String getComments() {
        return comments;
    }

    public List<TableColumnMeta> getColumns() {
        return columns;
    }

    public String getRemoteSchema() {
        return remoteSchema;
    }
}