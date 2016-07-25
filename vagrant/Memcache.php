<?php
return array (
  'stats_api' => 'Server',
  'slabs_api' => 'Server',
  'items_api' => 'Server',
  'get_api' => 'Server',
  'set_api' => 'Server',
  'delete_api' => 'Server',
  'flush_all_api' => 'Server',
  'connection_timeout' => '1',
  'max_item_dump' => '100',
  'refresh_rate' => 5,
  'memory_alert' => '80',
  'hit_rate_alert' => '90',
  'eviction_alert' => '0',
  'file_path' => 'Temp/',
  'servers' => 
  array (
    'Localhost' => 
    array (
      '127.0.0.1:11211' => array ('hostname' => '127.0.0.1', 'port' => '11211'),
	  '127.0.0.1:11212' => array ('hostname' => '127.0.0.1', 'port' => '11212'),
    ),
    'Production' => 
    array (
      'PROD lngsacprclsvc00' => 
      array (
        'hostname' => 'localhost',
        'port' => '11212',
      ),
    ),
    'QA' => 
    array (
	  'QA1 lngsacq1clmid00' => array ('hostname' => 'lngsacq1clmid00.examen.com', 'port' => '11211' ),
      'QA2 lngsacq2clmid00' => array ('hostname' => 'lngsacq2clmid00.examen.com', 'port' => '11211' ),
	  'QA3 lngsacq3clmid00' => array ('hostname' => 'lngsacq3clmid00.examen.com', 'port' => '11211' ),
	  'QA4 lngsacq4clmid00' => array ('hostname' => 'lngsacq4clmid00.examen.com', 'port' => '11211' ),
	  'QA5 lngsacq5clmid00' => array ('hostname' => 'lngsacq5clmid00.examen.com', 'port' => '11211' ),
	  'QA6 lngsacq6clmid00' => array ('hostname' => 'lngsacq6clmid00.examen.com', 'port' => '11211' ),
	  'QA7 lngsacq7clsvc00' => array ('hostname' => 'lngsacq7clsvc00.examen.com', 'port' => '11211' ),
    ),
  ),
);