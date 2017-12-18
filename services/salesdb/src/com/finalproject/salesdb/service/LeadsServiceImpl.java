/*Copyright (c) 2016-2017 wavemaker.com All Rights Reserved.
 This software is the confidential and proprietary information of wavemaker.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with wavemaker.com*/
package com.finalproject.salesdb.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.wavemaker.runtime.data.dao.WMGenericDao;
import com.wavemaker.runtime.data.exception.EntityNotFoundException;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.runtime.file.model.Downloadable;

import com.finalproject.salesdb.Leads;
import com.finalproject.salesdb.Quotes;


/**
 * ServiceImpl object for domain model class Leads.
 *
 * @see Leads
 */
@Service("salesdb.LeadsService")
@Validated
public class LeadsServiceImpl implements LeadsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeadsServiceImpl.class);

    @Lazy
    @Autowired
	@Qualifier("salesdb.QuotesService")
	private QuotesService quotesService;

    @Autowired
    @Qualifier("salesdb.LeadsDao")
    private WMGenericDao<Leads, Integer> wmGenericDao;

    public void setWMGenericDao(WMGenericDao<Leads, Integer> wmGenericDao) {
        this.wmGenericDao = wmGenericDao;
    }

    @Transactional(value = "salesdbTransactionManager")
    @Override
	public Leads create(Leads leads) {
        LOGGER.debug("Creating a new Leads with information: {}", leads);
        List<Quotes> quoteses = leads.getQuoteses();

        Leads leadsCreated = this.wmGenericDao.create(leads);
        if(quoteses != null) {
            for(Quotes _quotes : quoteses) {
                _quotes.setLeads(leadsCreated);
                LOGGER.debug("Creating a new child Quotes with information: {}", _quotes);
                quotesService.create(_quotes);
            }
        }
        return leadsCreated;
    }

	@Transactional(readOnly = true, value = "salesdbTransactionManager")
	@Override
	public Leads getById(Integer leadsId) throws EntityNotFoundException {
        LOGGER.debug("Finding Leads by id: {}", leadsId);
        Leads leads = this.wmGenericDao.findById(leadsId);
        if (leads == null){
            LOGGER.debug("No Leads found with id: {}", leadsId);
            throw new EntityNotFoundException(String.valueOf(leadsId));
        }
        return leads;
    }

    @Transactional(readOnly = true, value = "salesdbTransactionManager")
	@Override
	public Leads findById(Integer leadsId) {
        LOGGER.debug("Finding Leads by id: {}", leadsId);
        return this.wmGenericDao.findById(leadsId);
    }


	@Transactional(rollbackFor = EntityNotFoundException.class, value = "salesdbTransactionManager")
	@Override
	public Leads update(Leads leads) throws EntityNotFoundException {
        LOGGER.debug("Updating Leads with information: {}", leads);
        this.wmGenericDao.update(leads);

        Integer leadsId = leads.getId();

        return this.wmGenericDao.findById(leadsId);
    }

    @Transactional(value = "salesdbTransactionManager")
	@Override
	public Leads delete(Integer leadsId) throws EntityNotFoundException {
        LOGGER.debug("Deleting Leads with id: {}", leadsId);
        Leads deleted = this.wmGenericDao.findById(leadsId);
        if (deleted == null) {
            LOGGER.debug("No Leads found with id: {}", leadsId);
            throw new EntityNotFoundException(String.valueOf(leadsId));
        }
        this.wmGenericDao.delete(deleted);
        return deleted;
    }

	@Transactional(readOnly = true, value = "salesdbTransactionManager")
	@Override
	public Page<Leads> findAll(QueryFilter[] queryFilters, Pageable pageable) {
        LOGGER.debug("Finding all Leads");
        return this.wmGenericDao.search(queryFilters, pageable);
    }

    @Transactional(readOnly = true, value = "salesdbTransactionManager")
    @Override
    public Page<Leads> findAll(String query, Pageable pageable) {
        LOGGER.debug("Finding all Leads");
        return this.wmGenericDao.searchByQuery(query, pageable);
    }

    @Transactional(readOnly = true, value = "salesdbTransactionManager")
    @Override
    public Downloadable export(ExportType exportType, String query, Pageable pageable) {
        LOGGER.debug("exporting data in the service salesdb for table Leads to {} format", exportType);
        return this.wmGenericDao.export(exportType, query, pageable);
    }

	@Transactional(readOnly = true, value = "salesdbTransactionManager")
	@Override
	public long count(String query) {
        return this.wmGenericDao.count(query);
    }

    @Transactional(readOnly = true, value = "salesdbTransactionManager")
	@Override
    public Page<Map<String, Object>> getAggregatedValues(AggregationInfo aggregationInfo, Pageable pageable) {
        return this.wmGenericDao.getAggregatedValues(aggregationInfo, pageable);
    }

    @Transactional(readOnly = true, value = "salesdbTransactionManager")
    @Override
    public Page<Quotes> findAssociatedQuoteses(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated quoteses");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("leads.id = '" + id + "'");

        return quotesService.findAll(queryBuilder.toString(), pageable);
    }

    /**
	 * This setter method should only be used by unit tests
	 *
	 * @param service QuotesService instance
	 */
	protected void setQuotesService(QuotesService service) {
        this.quotesService = service;
    }

}

