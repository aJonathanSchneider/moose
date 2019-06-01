package de.shnyder.moose.timesheet.web;

import java.sql.Time;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.openapitools.api.TimeblocksApi;
import org.openapitools.model.ApiResultOkDeleteTimeBlocksModel;
import org.openapitools.model.ApiResultOkGetTimeBlockModel;
import org.openapitools.model.ApiResultOkGetTimeBlocksModel;
import org.openapitools.model.ApiResultOkPostTimeblocksModel;
import org.openapitools.model.ApiResultOkPutTimeBlocksModel;
import org.openapitools.model.ApiSubDeleteOkModel;
import org.openapitools.model.TimeBlockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import de.shnyder.moose.MooseError;
import de.shnyder.moose.timesheet.domain.model.TimeBlockDAO;
import de.shnyder.moose.timesheet.service.TimeBlockService;

@Controller
@RequestMapping("/")
@CrossOrigin("*")
// @CrossOrigin(origins={"http://localhost:3000", "http://127.0.0.1:3000"})
class TimeBlockController implements TimeblocksApi {

	protected String timeBlockApiVersion = "0.0.1";

	@Autowired
	private TimeBlockService timeBlockService;

	@Autowired
	private ModelMapper mapper;

	@Override
	public ResponseEntity<ApiResultOkPostTimeblocksModel> createTimeBlock(@Valid TimeBlockModel timeBlockModel) {
		TimeBlockDAO dao = convertToDao(timeBlockModel);
		ApiResultOkPostTimeblocksModel rv = new ApiResultOkPostTimeblocksModel();
		TimeBlockModel dto = convertToDto(timeBlockService.saveNewTimeBlock(dao, timeBlockModel.getUserId()));
		rv.setVersion(this.timeBlockApiVersion);
		rv.setResult(dto);
		return new ResponseEntity<ApiResultOkPostTimeblocksModel>(rv, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ApiResultOkDeleteTimeBlocksModel> deleteTimeBlockById(Long id) {
		timeBlockService.deleteTimeBlockById(id);
		ApiSubDeleteOkModel deleteModel = new ApiSubDeleteOkModel();
		deleteModel.deletedId(id);
		ApiResultOkDeleteTimeBlocksModel rv = new ApiResultOkDeleteTimeBlocksModel();
		rv.setVersion(this.timeBlockApiVersion);
		rv.setResult(deleteModel);
		return new ResponseEntity<ApiResultOkDeleteTimeBlocksModel>(rv, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ApiResultOkGetTimeBlocksModel> listTimeBlocks(@Valid OffsetDateTime since,
			@Valid OffsetDateTime until) {
		ApiResultOkGetTimeBlocksModel rv = new ApiResultOkGetTimeBlocksModel();
		rv.setVersion(this.timeBlockApiVersion);
		List<TimeBlockModel> targetCollection = new ArrayList<TimeBlockModel>();
		if (since == null && until == null) {
			this.timeBlockService.listAllTimeBlocks().forEach((dao) -> {
				targetCollection.add(this.convertToDto(dao));
			});
		} else {
			Time sinceSql;
			Time untilSql;
			if (since == null) {
				java.util.Date today = new java.util.Date();
				today.setYear(today.getYear() - 1);
				sinceSql = new Time(today.getTime());
			} else {
				sinceSql = new Time(since.toEpochSecond() * 1000);
			}
			if (until == null) {
				java.util.Date today = new java.util.Date();
				untilSql = new Time(today.getTime());
			} else {
				untilSql = new Time(until.toEpochSecond() * 1000);
			}
			this.timeBlockService.listTimeBlocksBetween(sinceSql, untilSql).forEach((dao) -> {
				targetCollection.add(this.convertToDto(dao));
			});
		}
		targetCollection.add(new TimeBlockModel().description("test").endTime(OffsetDateTime.now()));
		rv.setResult(targetCollection);
		return new ResponseEntity<ApiResultOkGetTimeBlocksModel>(rv, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ApiResultOkGetTimeBlockModel> showTimeBlockById(Long id) {
		ApiResultOkGetTimeBlockModel rv = new ApiResultOkGetTimeBlockModel();
		rv.version(this.timeBlockApiVersion).result(convertToDto(timeBlockService.getTimeBlockById(id)));
		return new ResponseEntity<ApiResultOkGetTimeBlockModel>(rv, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ApiResultOkPutTimeBlocksModel> updateTimeBlockById(Long id,
			@Valid TimeBlockModel timeBlockModel) {
		ApiResultOkPutTimeBlocksModel rv = new ApiResultOkPutTimeBlocksModel();
		if (id != timeBlockModel.getId())
			throw new MooseError("path id and body id not equal", MooseError.ERR_USERS_FAULT_USERCANFIX);
		rv.version(timeBlockApiVersion)
				.result(convertToDto(timeBlockService.updateTimeBlock(convertToDao(timeBlockModel))));
		return TimeblocksApi.super.updateTimeBlockById(id, timeBlockModel);
	}

	private TimeBlockModel convertToDto(TimeBlockDAO dao) {
		TimeBlockModel dto = mapper.map(dao, TimeBlockModel.class);
		return dto;
	}

	private TimeBlockDAO convertToDao(TimeBlockModel dto) {
		TimeBlockDAO dao = mapper.map(dto, TimeBlockDAO.class);
		return dao;
	}
}