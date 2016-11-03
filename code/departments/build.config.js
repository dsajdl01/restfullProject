/**
 * This file/module contains all configuration for the build process.
 */
module.exports = {
  /**
   * The `build_dir` folder is where our projects are compiled during
   * development and the `compile_dir` folder is where our app resides once it's
   * completely built.
   */
  tmp_dir: 'target/tmp',
  target_webapp_dir: 'target/tmp',
  webapp_dir: 'src/main/webapp',
  views_dir: '<%= webapp_dir %>/angularViews',
  vendor_dir: '<%= webapp_dir %>/vendor',
  script_dest_dir: '<%= webapp_dir %>/scripts',
  styles_dest_dir: '<%= webapp_dir %>/styles'

};