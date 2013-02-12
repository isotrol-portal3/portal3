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

DROP TABLE CATEGORY CASCADE CONSTRAINT PURGE;
DROP TABLE CATEGORY_DFN CASCADE CONSTRAINT PURGE;
DROP TABLE CATEGORY_EDITION CASCADE CONSTRAINT PURGE;
DROP TABLE CATEGORY_L7D CASCADE CONSTRAINT PURGE;
DROP TABLE CATEGORY_MAPPING CASCADE CONSTRAINT PURGE;

DROP TABLE COMPONENT CASCADE CONSTRAINT PURGE;
DROP TABLE COMPONENT_DFN CASCADE CONSTRAINT PURGE;
DROP TABLE COMPONENT_DFN_DEPS CASCADE CONSTRAINT PURGE;

DROP TABLE CONFIGURATION CASCADE CONSTRAINT PURGE;
DROP TABLE CONFIGURATION_VALUE CASCADE CONSTRAINT PURGE;

DROP TABLE CONNECTOR CASCADE CONSTRAINT PURGE;
DROP TABLE CONNECTOR_DEPS CASCADE CONSTRAINT PURGE;
DROP TABLE CONNECTOR_DFN CASCADE CONSTRAINT PURGE;
DROP TABLE CONNECTOR_EDITION CASCADE CONSTRAINT PURGE;

DROP TABLE CONTENT_TYPE CASCADE CONSTRAINT PURGE;
DROP TABLE CONTENT_TYPE_DFN CASCADE CONSTRAINT PURGE;
DROP TABLE CONTENT_TYPE_EDITION CASCADE CONSTRAINT PURGE;
DROP TABLE CONTENT_TYPE_L7D CASCADE CONSTRAINT PURGE;
DROP TABLE CONTENT_TYPE_MAPPING CASCADE CONSTRAINT PURGE;

DROP TABLE DEVICE CASCADE CONSTRAINT PURGE;
DROP TABLE EDITION CASCADE CONSTRAINT PURGE;
DROP TABLE ENVIRONMENT CASCADE CONSTRAINT PURGE;
DROP TABLE FILE_DATA CASCADE CONSTRAINT PURGE;

DROP TABLE PAGE CASCADE CONSTRAINT PURGE;
DROP TABLE PAGE_CIP CASCADE CONSTRAINT PURGE;
DROP TABLE PAGE_DFN CASCADE CONSTRAINT PURGE;
DROP TABLE PAGE_LAYOUT CASCADE CONSTRAINT PURGE;

DROP TABLE PMS_USER CASCADE CONSTRAINT PURGE;
DROP TABLE PMS_USER_GA CASCADE CONSTRAINT PURGE;
DROP TABLE PMS_USER_GROLE CASCADE CONSTRAINT PURGE;
DROP TABLE PMS_USER_PA CASCADE CONSTRAINT PURGE;

DROP TABLE PORTAL CASCADE CONSTRAINT PURGE;
DROP TABLE PORTAL_BASE CASCADE CONSTRAINT PURGE;
DROP TABLE PORTAL_CATEGORY CASCADE CONSTRAINT PURGE;
DROP TABLE PORTAL_COMPONENT_DFN CASCADE CONSTRAINT PURGE;
DROP TABLE PORTAL_CONTENT_TYPE CASCADE CONSTRAINT PURGE;
DROP TABLE PORTAL_DFN CASCADE CONSTRAINT PURGE;
DROP TABLE PORTAL_EDITION CASCADE CONSTRAINT PURGE;
DROP TABLE PORTAL_PAGE_DFN CASCADE CONSTRAINT PURGE;
DROP TABLE PORTAL_PROPERTY CASCADE CONSTRAINT PURGE;

DROP TABLE ROUTING_DOMAIN CASCADE CONSTRAINT PURGE;
DROP TABLE SOURCE_MAPPING CASCADE CONSTRAINT PURGE;
