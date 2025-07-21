/**
 * @package com.nopaper.work.gateway.entity -> gateway
 * @author saikatbarman
 * @date 2025 05-Jul-2025 1:51:29 am
 * @git 
 */
package com.nopaper.work.gateway.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */


@Getter
@Setter
@Document("apiRoutes")
public class ApiRoute {
    @Id
    private String id;
    private String routeIdentifier;
    private String uri;
    private String method;
    private String path;
}

/*
@Getter
@Setter
@NoArgsConstructor
@Table(name = "api_router", schema = "npw")
public class ApiRoute implements Serializable {

	private static final long serialVersionUID = 4907362713286525406L;

	@Id
//	@GeneratedValue(strategy = GenerationType.UUID)
//	@Column(name = "id", unique = true, updatable = false, nullable = false)
	private UUID id;
	
	@NotBlank
	@Size(max = 100)
	private String routeIdentifier;
	
	@NotBlank
	@Size(max = 256)
	private String uri;
	
	@NotBlank
	@Size(max = 256)
	private String method;
	
	@NotBlank
	@Size(max = 512)
	private String path;
	
	@NotBlank
	@Size(max = 50)
	private String role;
	
	@Type(JsonType.class)
    private Map<String, String> globalRule;
}
*/