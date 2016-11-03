// This file is used to control Grunt (think Ant for javascript)

// You can run this script (from project root) using:

// This can be run manually by installing grunt (bower bower-install-simple for library setup - you will need to have bower installed) or from maven using the frontend-maven-plugin (see pom)

module.exports = function ( grunt ) {

  /**
   * Load required Grunt tasks. These are installed based on the versions listed
   * in `package.json` when you do `npm install` in this directory.
   */
	require('load-grunt-tasks')(grunt);

  var userConfig = require( './build.config.js' );

  /**
   * This is the configuration object Grunt uses to give each plugin its
   * instructions.
   */
  var taskConfig = {

		// Downloads all the javascript library dependencies as defined in bower.json.
	    "bower-install-simple": {
	        options: {
	            color: true,
	            directory: "<%= vendor_dir %>/"
	        },
	        "prod": {
	            options: {
	                production: true
	            }
	        },
	        "dev": {
	            options: {
	                production: false
	            }
	        }
	    },
	};

	grunt.initConfig( grunt.util._.extend( taskConfig, userConfig ) );

	grunt.registerTask("bower-install", [ "bower-install-simple" ]);


  /**
   * The default task is to build and compile.
   */
  grunt.registerTask( 'default', [ 'clean', 'build' ] );

  /**
   * The `build` task gets your app ready to run for development and testing.
   */
  grunt.registerTask('build', []);
};