--
-- This file is part of Port@l
-- Port@l 3.0 - Portal Engine and Management System
-- Copyright (C) 2010  Isotrol, SA.  http://www.isotrol.com
--
-- Port@l is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- Port@l is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with Port@l.  If not, see <http://www.gnu.org/licenses/>.
--

-- Table: ENVIRONMENT
DROP INDEX IDX_ENVIRONMENT_FK1;

-- Table: EDITION
DROP INDEX IDX_EDITION_FK1;

-- Table: CONTENT_TYPE
DROP INDEX IDX_CONTENT_TYPE_FK1;
DROP INDEX IDX_CONTENT_TYPE_FK2;

-- Table: CONTENT_TYPE_DFN
DROP INDEX IDX_CONTENT_TYPE_DFN_FK1;

-- Table: CONTENT_TYPE_L7D
DROP INDEX IDX_CONTENT_TYPE_L7D_FK1;

-- Table: CONTENT_TYPE_EDITION
DROP INDEX IDX_CONTENT_TYPE_EDITION_FK1;
DROP INDEX IDX_CONTENT_TYPE_EDITION_FK2;

-- Table: CATEGORY
DROP INDEX IDX_CATEGORY_FK1;
DROP INDEX IDX_CATEGORY_FK2;

-- Table: CATEGORY_DFN
DROP INDEX IDX_CATEGORY_DFN_FK1;
DROP INDEX IDX_CATEGORY_DFN_FK2;

-- Table: CATEGORY_L7D
DROP INDEX IDX_CATEGORY_L7D_FK1;

-- Table: CATEGORY_EDITION
DROP INDEX IDX_CATEGORY_EDITION_FK1;
DROP INDEX IDX_CATEGORY_EDITION_FK2;

-- Table: SOURCE_MAPPING
DROP INDEX IDX_SOURCE_MAPPING_FK1;

-- Table: ROUTING_DOMAIN
DROP INDEX IDX_ROUTING_DOMAIN_FK1;

-- Table: SET_MAPPING
DROP INDEX IDX_SET_MAPPING_FK1;

-- Table: CONTENT_TYPE_MAPPING
DROP INDEX IDX_CONTENT_TYPE_MAPPING_FK1;
DROP INDEX IDX_CONTENT_TYPE_MAPPING_FK2;

-- Table: CATEGORY_MAPPING
DROP INDEX IDX_CATEGORY_MAPPING_FK1;
DROP INDEX IDX_CATEGORY_MAPPING_FK2;

-- Table: CONNECTOR
DROP INDEX IDX_CONNECTOR_FK1;
DROP INDEX IDX_CONNECTOR_FK2;

-- Table: CONNECTOR_DFN
DROP INDEX IDX_CONNECTOR_DFN_FK1;
DROP INDEX IDX_CONNECTOR_DFN_FK2;

-- Table: CONNECTOR_DEPS
DROP INDEX IDX_CONNECTOR_DEPS_FK1;
DROP INDEX IDX_CONNECTOR_DEPS_FK2;

-- Table: CONNECTOR_EDITION
DROP INDEX IDX_CONNECTOR_EDITION_FK1;
DROP INDEX IDX_CONNECTOR_EDITION_FK2;

-- Table: CONFIGURATION_VALUE
DROP INDEX IDX_CONFIGURATION_VALUE_FK1;
DROP INDEX IDX_CONFIGURATION_VALUE_FK2;
DROP INDEX IDX_CONFIGURATION_VALUE_FK3;
DROP INDEX IDX_CONFIGURATION_VALUE_FK4;

-- Table: DEVICE
DROP INDEX IDX_DEVICE_FK1;

-- Table: DEVICE_PROPERTY
DROP INDEX IDX_DEVICE_PROPERTY_FK1;

-- Table: PORTAL
DROP INDEX IDX_PORTAL_FK1;
DROP INDEX IDX_PORTAL_FK2;
DROP INDEX IDX_PORTAL_FK3;

-- Table: PORTAL_DFN
DROP INDEX IDX_PORTAL_DFN_FK1;
DROP INDEX IDX_PORTAL_DFN_FK2;
DROP INDEX IDX_PORTAL_DFN_FK3;
DROP INDEX IDX_PORTAL_DFN_FK4;
DROP INDEX IDX_PORTAL_DFN_FK5;
DROP INDEX IDX_PORTAL_DFN_FK6;
DROP INDEX IDX_PORTAL_DFN_FK7;
DROP INDEX IDX_PORTAL_DFN_FK8;

-- Table: PORTAL_EDITION
DROP INDEX IDX_PORTAL_EDITION_FK1;
DROP INDEX IDX_PORTAL_EDITION_FK2;

-- Table: PORTAL_BASE
DROP INDEX IDX_PORTAL_BASE_FK1;

-- Table: PORTAL_PROPERTY
DROP INDEX IDX_PORTAL_PROPERTY_FK1;

-- Table: PORTAL_COMPONENT_DFN
DROP INDEX IDX_PORTAL_COMPONENT_DFN_FK1;

-- Table: PORTAL_PAGE_DFN
DROP INDEX IDX_PORTAL_PAGE_DFN_FK1;

-- Table: PORTAL_LOCALE
DROP INDEX IDX_PORTAL_LOCALE_FK1;

-- Table: PORTAL_AVAILABLE_LOCALE
DROP INDEX IDX_PORTAL_AVAIL_LOC_FK1;

-- Table: PORTAL_COMPONENT_OVERRIDE
DROP INDEX IDX_PORTAL_COMP_OVER_FK1;
DROP INDEX IDX_PORTAL_COMP_OVER_FK2;

-- Table: PORTAL_SET_FILTER
DROP INDEX IDX_PORTAL_SET_FILTER_FK1;

-- Table: PORTAL_DEVICE

-- Table: PMS_DEPENDENCY_ITEM
DROP INDEX IDX_PMS_DEPENDENCY_ITEM_FK1;
DROP INDEX IDX_PMS_DEPENDENCY_ITEM_FK2;

-- Table: COMPONENT
DROP INDEX IDX_COMPONENT_FK1;

-- Table: COMPONENT_DFN
DROP INDEX IDX_COMPONENT_DFN_FK1;
DROP INDEX IDX_COMPONENT_DFN_FK2;

-- Table: PAGE
DROP INDEX IDX_PAGE_FK1;
DROP INDEX IDX_PAGE_FK2;

-- Table: PAGE_DFN
DROP INDEX IDX_PAGE_DFN_FK1;
DROP INDEX IDX_PAGE_DFN_FK2;
DROP INDEX IDX_PAGE_DFN_FK3;
DROP INDEX IDX_PAGE_DFN_FK4;

-- Table: PAGE_CIP
DROP INDEX IDX_PAGE_CIP_FK1;
DROP INDEX IDX_PAGE_CIP_FK2;
DROP INDEX IDX_PAGE_CIP_FK3;

-- Table: PAGE_LAYOUT
DROP INDEX IDX_PAGE_LAYOUT_FK1;

