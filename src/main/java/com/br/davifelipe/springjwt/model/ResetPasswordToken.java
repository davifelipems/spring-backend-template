package com.br.davifelipe.springjwt.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString(exclude="id")
@EqualsAndHashCode(exclude={"token","createdDate"})
public class ResetPasswordToken implements Serializable{

	private static final long serialVersionUID = -2747634784133926811L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String token;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
	
	public boolean isExpired(Long resetPasswordTokenExpirationMisiseg) {
		Calendar timeout = Calendar.getInstance();
		timeout.setTimeInMillis(this.createdDate.getTime() + resetPasswordTokenExpirationMisiseg);
		Date dateTimeout = timeout.getTime();
		return (dateTimeout.before(new Date()));
	}
	
	
}
