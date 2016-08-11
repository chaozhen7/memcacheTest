xmemcache一般配合spring使用
...
<bean id="memcachedClientBuilder" class="net.rubyeye.xmemcached.XMemcachedClientBuilder"
			p:connectionPoolSize="${memcached.connectionPoolSize}"
			p:failureMode="${memcached.failureMode}">
			
			<constructor-arg>
				<list>
					<bean class="java.net.InetSocketAddress">
						<constructor-arg>
							<value>${memcached.server1.host}</value>
						</constructor-arg>
						<constructor-arg>
							<value>${memcached.server1.port}</value>
						</constructor-arg>
					</bean>
					<!-- <bean class="java.net.InetSocketAddress">
						<constructor-arg>
							<value>${memcached.server2.host}</value>
						</constructor-arg>
						<constructor-arg>
							<value>${memcached.server2.port}</value>
						</constructor-arg>
					</bean>
					<bean class="java.net.InetSocketAddress">
						<constructor-arg>
							<value>${memcached.server3.host}</value>
						</constructor-arg>
						<constructor-arg>
							<value>${memcached.server3.port}</value>
						</constructor-arg>
					</bean>
					<bean class="java.net.InetSocketAddress">
						<constructor-arg>
							<value>${memcached.server4.host}</value>
						</constructor-arg>
						<constructor-arg>
							<value>${memcached.server4.port}</value>
						</constructor-arg>
					</bean> -->
				</list>
			</constructor-arg>	
			<constructor-arg>
				<list>
					<value>${memcached.server1.weight}</value>
					<!-- <value>${memcached.server2.weight}</value>
					<value>${memcached.server3.weight}</value>
					<value>${memcached.server4.weight}</value> -->
				</list>
			</constructor-arg>	
			<property name="commandFactory">
				<bean class="net.rubyeye.xmemcached.command.TextCommandFactory" />
			</property>
			<property name="sessionLocator">  
            	<bean class="net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator" />  
        	</property>  
        	<property name="transcoder">  
            	<bean class="net.rubyeye.xmemcached.transcoders.SerializingTranscoder" />  
        	</property>  
	</bean>
	
	<bean id="XmemcachedClient"
		  factory-bean="memcachedClientBuilder"
		  factory-method="build"
		  destroy-method="shutdown"/>	
...