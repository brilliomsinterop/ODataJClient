/*
 * Copyright 2013 MS OpenTech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.msopentech.odatajclient.engine.data.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.msopentech.odatajclient.engine.data.EntryResource;
import com.msopentech.odatajclient.engine.data.LinkResource;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;

/**
 * A single entry, represented via JSON.
 */
@JsonSerialize(using = JSONEntrySerializer.class)
@JsonDeserialize(using = JSONEntryDeserializer.class)
public class JSONEntry extends AbstractJSONMetadataObject implements EntryResource {

    private static final long serialVersionUID = -5275365545400797758L;

    private URI metadata;

    private String type;

    private String id;

    private String etag;

    private JSONLink readLink;

    private JSONLink editLink;

    private List<JSONLink> associationLinks;

    private List<JSONLink> navigationLinks;

    private List<JSONLink> mediaEditLinks;

    private Element content;

    private Element mediaEntryProperties;

    private String mediaContentSource;

    private String mediaContentType;

    public JSONEntry() {
        associationLinks = new ArrayList<JSONLink>();
        navigationLinks = new ArrayList<JSONLink>();
        mediaEditLinks = new ArrayList<JSONLink>();
    }

    @Override
    public URI getMetadata() {
        return metadata;
    }

    public void setMetadata(final URI metadata) {
        this.metadata = metadata;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public String getEtag() {
        return etag;
    }

    @Override
    public void setEtag(final String etag) {
        this.etag = etag;
    }

    @Override
    public LinkResource getSelfLink() {
        return readLink;
    }

    @Override
    public boolean setSelfLink(final LinkResource readLink) {
        final boolean result = (readLink instanceof JSONLink);
        if (result) {
            this.readLink = (JSONLink) readLink;
        }

        return result;
    }

    @Override
    public LinkResource getEditLink() {
        return editLink;
    }

    @Override
    public boolean setEditLink(LinkResource editLink) {
        final boolean result = (editLink instanceof JSONLink);
        if (result) {
            this.editLink = (JSONLink) editLink;
        }

        return result;
    }

    @Override
    public boolean addAssociationLink(final LinkResource link) {
        return (link instanceof JSONLink) ? associationLinks.add((JSONLink) link) : false;
    }

    @Override
    public List<JSONLink> getAssociationLinks() {
        return associationLinks;
    }

    private void setLinks(final List<JSONLink> links, final List<LinkResource> linkResources) {
        links.clear();
        for (LinkResource link : linkResources) {
            if (link instanceof JSONLink) {
                links.add((JSONLink) link);
            }
        }
    }

    @Override
    public boolean addNavigationLink(final LinkResource link) {
        return (link instanceof JSONLink) ? navigationLinks.add((JSONLink) link) : false;
    }

    @Override
    public void setAssociationLinks(final List<LinkResource> associationLinks) {
        setLinks(this.associationLinks, associationLinks);
    }

    @Override
    public List<JSONLink> getNavigationLinks() {
        return navigationLinks;
    }

    @Override
    public boolean addMediaEditLink(final LinkResource link) {
        return (link instanceof JSONLink) ? mediaEditLinks.add((JSONLink) link) : false;
    }

    @Override
    public void setNavigationLinks(final List<LinkResource> navigationLinks) {
        setLinks(this.navigationLinks, navigationLinks);
    }

    @Override
    public List<JSONLink> getMediaEditLinks() {
        return mediaEditLinks;
    }

    @Override
    public void setMediaEditLinks(final List<LinkResource> mediaEditLinks) {
        setLinks(this.mediaEditLinks, mediaEditLinks);
    }

    @Override
    public Element getContent() {
        return content;
    }

    @Override
    public void setContent(final Element content) {
        this.content = content;
    }

    @Override
    public Element getMediaEntryProperties() {
        return mediaEntryProperties;
    }

    @Override
    public void setMediaEntryProperties(final Element mediaEntryProperties) {
        this.mediaEntryProperties = mediaEntryProperties;
    }

    @Override
    public String getMediaContentType() {
        return this.mediaContentType;
    }

    public void setMediaContentType(final String mediaContentType) {
        this.mediaContentType = mediaContentType;
    }

    @Override
    public String getMediaContentSource() {
        return this.mediaContentSource;
    }

    public void setMediaContentSource(final String mediaContentSource) {
        this.mediaContentSource = mediaContentSource;
    }

    @Override
    public void setMediaContent(final String mediaContentSource, final String mediaContentType) {
        setMediaContentSource(mediaContentSource);
        setMediaContentType(mediaContentType);
    }

    @Override
    public boolean isMediaEntry() {
        return StringUtils.isNotBlank(this.mediaContentSource);
    }
}
