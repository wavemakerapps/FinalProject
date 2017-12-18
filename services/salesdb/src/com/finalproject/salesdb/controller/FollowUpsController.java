/*Copyright (c) 2016-2017 wavemaker.com All Rights Reserved.
 This software is the confidential and proprietary information of wavemaker.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with wavemaker.com*/
package com.finalproject.salesdb.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wavemaker.runtime.data.exception.EntityNotFoundException;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.runtime.file.model.Downloadable;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import com.finalproject.salesdb.FollowUps;
import com.finalproject.salesdb.service.FollowUpsService;


/**
 * Controller object for domain model class FollowUps.
 * @see FollowUps
 */
@RestController("salesdb.FollowUpsController")
@Api(value = "FollowUpsController", description = "Exposes APIs to work with FollowUps resource.")
@RequestMapping("/salesdb/FollowUps")
public class FollowUpsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FollowUpsController.class);

    @Autowired
	@Qualifier("salesdb.FollowUpsService")
	private FollowUpsService followUpsService;

	@ApiOperation(value = "Creates a new FollowUps instance.")
    @RequestMapping(method = RequestMethod.POST)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public FollowUps createFollowUps(@RequestBody FollowUps followUps) {
		LOGGER.debug("Create FollowUps with information: {}" , followUps);

		followUps = followUpsService.create(followUps);
		LOGGER.debug("Created FollowUps with information: {}" , followUps);

	    return followUps;
	}

    @ApiOperation(value = "Returns the FollowUps instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public FollowUps getFollowUps(@PathVariable("id") Integer id) throws EntityNotFoundException {
        LOGGER.debug("Getting FollowUps with id: {}" , id);

        FollowUps foundFollowUps = followUpsService.getById(id);
        LOGGER.debug("FollowUps details with id: {}" , foundFollowUps);

        return foundFollowUps;
    }

    @ApiOperation(value = "Updates the FollowUps instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PUT)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public FollowUps editFollowUps(@PathVariable("id") Integer id, @RequestBody FollowUps followUps) throws EntityNotFoundException {
        LOGGER.debug("Editing FollowUps with id: {}" , followUps.getId());

        followUps.setId(id);
        followUps = followUpsService.update(followUps);
        LOGGER.debug("FollowUps details with id: {}" , followUps);

        return followUps;
    }

    @ApiOperation(value = "Deletes the FollowUps instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.DELETE)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public boolean deleteFollowUps(@PathVariable("id") Integer id) throws EntityNotFoundException {
        LOGGER.debug("Deleting FollowUps with id: {}" , id);

        FollowUps deletedFollowUps = followUpsService.delete(id);

        return deletedFollowUps != null;
    }

    /**
     * @deprecated Use {@link #findFollowUps(String, Pageable)} instead.
     */
    @Deprecated
    @ApiOperation(value = "Returns the list of FollowUps instances matching the search criteria.")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<FollowUps> searchFollowUpsByQueryFilters( Pageable pageable, @RequestBody QueryFilter[] queryFilters) {
        LOGGER.debug("Rendering FollowUps list");
        return followUpsService.findAll(queryFilters, pageable);
    }

    @ApiOperation(value = "Returns the paginated list of FollowUps instances matching the optional query (q) request param. If there is no query provided, it returns all the instances. Pagination & Sorting parameters such as page& size, sort can be sent as request parameters. The sort value should be a comma separated list of field names & optional sort order to sort the data on. eg: field1 asc, field2 desc etc ")
    @RequestMapping(method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<FollowUps> findFollowUps(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
        LOGGER.debug("Rendering FollowUps list");
        return followUpsService.findAll(query, pageable);
    }

    @ApiOperation(value = "Returns the paginated list of FollowUps instances matching the optional query (q) request param. This API should be used only if the query string is too big to fit in GET request with request param. The request has to made in application/x-www-form-urlencoded format.")
    @RequestMapping(value="/filter", method = RequestMethod.POST, consumes= "application/x-www-form-urlencoded")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<FollowUps> filterFollowUps(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
        LOGGER.debug("Rendering FollowUps list");
        return followUpsService.findAll(query, pageable);
    }

    @ApiOperation(value = "Returns downloadable file for the data matching the optional query (q) request param. If query string is too big to fit in GET request's query param, use POST method with application/x-www-form-urlencoded format.")
    @RequestMapping(value = "/export/{exportType}", method = {RequestMethod.GET,  RequestMethod.POST}, produces = "application/octet-stream")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Downloadable exportFollowUps(@PathVariable("exportType") ExportType exportType, @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
         return followUpsService.export(exportType, query, pageable);
    }

	@ApiOperation(value = "Returns the total count of FollowUps instances matching the optional query (q) request param. If query string is too big to fit in GET request's query param, use POST method with application/x-www-form-urlencoded format.")
	@RequestMapping(value = "/count", method = {RequestMethod.GET, RequestMethod.POST})
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
	public Long countFollowUps( @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query) {
		LOGGER.debug("counting FollowUps");
		return followUpsService.count(query);
	}

    @ApiOperation(value = "Returns aggregated result with given aggregation info")
	@RequestMapping(value = "/aggregations", method = RequestMethod.POST)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
	public Page<Map<String, Object>> getFollowUpsAggregatedValues(@RequestBody AggregationInfo aggregationInfo, Pageable pageable) {
        LOGGER.debug("Fetching aggregated results for {}", aggregationInfo);
        return followUpsService.getAggregatedValues(aggregationInfo, pageable);
    }


    /**
	 * This setter method should only be used by unit tests
	 *
	 * @param service FollowUpsService instance
	 */
	protected void setFollowUpsService(FollowUpsService service) {
		this.followUpsService = service;
	}

}

