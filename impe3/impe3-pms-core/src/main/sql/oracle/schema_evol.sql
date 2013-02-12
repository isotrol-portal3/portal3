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

--
-- Schema evolution for Oracle
--

--
-- Version 2.9.159 to 2.9.160
--

ALTER TABLE PORTAL_DEVICE RENAME COLUMN MAPKEY_ID TO PRDV_DVCE_ID;
ALTER TABLE PORTAL_COMPONENT_OVERRIDE RENAME COLUMN MAPKEY_ID TO PRTL_CMPT_ID;

--
-- Version 2.9.181 to 2.9.182
--

ALTER TABLE CATEGORY ADD EVER_PUBLISHED NUMBER(1,0);
ALTER TABLE CATEGORY_DFN ADD EVER_PUBLISHED NUMBER(1,0);
ALTER TABLE CONNECTOR ADD EVER_PUBLISHED NUMBER(1,0);
ALTER TABLE CONNECTOR_DFN ADD EVER_PUBLISHED NUMBER(1,0);
ALTER TABLE CONTENT_TYPE ADD EVER_PUBLISHED NUMBER(1,0);
ALTER TABLE CONTENT_TYPE_DFN ADD EVER_PUBLISHED NUMBER(1,0);
ALTER TABLE PORTAL ADD EVER_PUBLISHED NUMBER(1,0);
ALTER TABLE PORTAL_DFN ADD EVER_PUBLISHED NUMBER(1,0);
