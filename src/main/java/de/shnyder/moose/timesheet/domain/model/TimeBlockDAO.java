package de.shnyder.moose.timesheet.domain.model;

import java.sql.Time;
import java.time.OffsetDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.shnyder.moose.user.domain.model.BaseUserDataDAO;

/**
 * UserDAO
 */
@Entity
@Table(name = "TimeBlock")
public class TimeBlockDAO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	protected OffsetDateTime startTime;
	
	@NotNull
	protected OffsetDateTime endTime;

	@ManyToOne(cascade = CascadeType.ALL, targetEntity = BaseUserDataDAO.class)
	@JoinColumn(
        name = "userId",
        referencedColumnName = "id"
    )
	@NotNull
	protected BaseUserDataDAO user;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	public OffsetDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(OffsetDateTime startTime) {
		this.startTime = startTime;
	}

	public OffsetDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(OffsetDateTime endTime) {
		this.endTime = endTime;
	}

	public BaseUserDataDAO getUser() {
		return user;
	}

	public void setUser(BaseUserDataDAO user) {
		this.user = user;
	}
}