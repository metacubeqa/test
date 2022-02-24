/**
 * 
 */
package support.source.conversationAIReact;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;

/**
 * @author Abhishek Gupta
 *
 */
public class LibraryReactPage extends SeleniumBase{
	Dashboard dashboard 	= new Dashboard();
	CallsTabReactPage callsTabReactPage = new CallsTabReactPage();
	
	String noResultImgOuterHtml		= "<svg width=\"111\" height=\"111\" viewBox=\"0 0 111 111\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\" style=\"position: relative; left: 15.96px;\"><path fill-rule=\"evenodd\" clip-rule=\"evenodd\" d=\"M104.913 78.1351C105.01 77.9426 104.972 77.7101 104.82 77.5579L90.6152 63.3534C90.453 63.1912 90.2012 63.1602 90.0044 63.2782L79.9796 69.2931C79.873 69.3571 79.7536 69.3805 79.6326 69.3517C78.1898 69.0085 68.3215 66.3204 56.6582 54.6571C44.9949 42.9938 42.3069 33.1256 41.9636 31.6828C41.9348 31.5618 41.9583 31.4424 42.0223 31.3357L48.0371 21.3109C48.1552 21.1142 48.1242 20.8623 47.9619 20.7001L33.7574 6.49557C33.6052 6.34339 33.3727 6.30566 33.1802 6.40191L16.9153 14.5344C16.7582 14.6129 16.6547 14.7594 16.6469 14.9348C16.5168 17.8533 16.1532 43.6229 41.9228 69.3925C67.6924 95.1622 93.4621 94.7986 96.3805 94.6685C96.556 94.6606 96.7024 94.5571 96.781 94.4L104.913 78.1351Z\" stroke=\"#7549EA\" stroke-width=\"2.5\"></path></svg>";
	String noSearchResultOuterHtml 	= "<svg width=\"332\" height=\"237\" viewBox=\"0 0 332 237\" fill=\"none\"><g clip-path=\"url(#prefix__clip0)\"><path d=\"M281.239 193.286s5.363-11.956 3.368-22.542c-1.871-10.336-8.606-8.095-9.105-17.933-.997-16.191-12.097-13.077-13.095-24.037-1.123-11.582-11.225-11.208-16.588-16.439-5.238-5.231-11.224-7.597-13.719-5.106-4.24 4.234-.124 10.959-1.995 18.556-1.497 5.729-.998 15.443 4.615 19.927 7.233 5.853-2.37 14.571 10.476 22.417 14.218 8.344.125 19.802 36.043 25.157z\" fill=\"#3CA5A5\"></path><path d=\"M281.115 193.411c3.242 15.443 3.866 40.849 3.866 40.849\" stroke=\"#3CA5A5\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M242.078 121.427c11.1 11.084 35.295 55.669 38.912 71.486\" stroke=\"#fff\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M51.883 208.231s.748-8.967-2.869-15.567c-3.492-6.352-7.358-3.363-9.977-9.714-4.24-10.337-10.85-5.854-13.968-12.828-3.368-7.348-9.853-4.857-14.592-7.099-4.74-2.242-8.98-2.491-10.103-.374-1.746 3.737 2.37 7.224 2.869 12.579.374 4.11 2.868 10.337 7.483 11.956 5.986 2.117 1.746 10.088 11.848 12.329 11.35 2.616 4.615 13.326 29.309 8.718z\" fill=\"#3CA5A5\"></path><path d=\"M51.883 208.355c5.612 9.341 11.723 25.905 11.723 25.905\" stroke=\"#3CA5A5\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M10.227 169.998c9.853 4.732 35.67 28.519 41.532 37.984\" stroke=\"#fff\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M303.689 222.179s-.374-4.234 1.372-7.223c1.622-2.989 3.492-1.619 4.615-4.483 1.995-4.857 4.989-2.74 6.485-5.978 1.497-3.363 4.615-2.242 6.735-3.238 2.12-.997 4.24-1.121 4.739-.125.873 1.744-1.122 3.363-1.372 5.854-.124 1.868-1.372 4.732-3.492 5.604-2.868.996-.873 4.732-5.487 5.729-5.114.996-1.996 5.978-13.595 3.86z\" fill=\"#3C9\"></path><path d=\"M303.689 222.179c-2.619 4.359-5.487 12.081-5.487 12.081\" stroke=\"#3C9\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M323.145 204.37c-4.614 2.242-16.587 13.201-19.331 17.685\" stroke=\"#fff\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M274.131 214.209s.623-6.974-2.245-11.956c-2.744-4.857-5.737-2.615-7.733-7.472-3.243-7.971-8.356-4.484-10.726-9.839-2.494-5.604-7.607-3.736-11.224-5.48-3.617-1.743-6.984-1.868-7.733-.249-1.372 2.864 1.871 5.48 2.245 9.714.25 3.114 2.245 7.971 5.737 9.216 4.615 1.619 1.372 7.722 9.105 9.465 8.73 1.868 3.616 10.088 22.574 6.601z\" fill=\"#3C9\"></path><path d=\"M274.131 214.333c6.984 9.466 10.85 19.927 10.85 19.927\" stroke=\"#3C9\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M242.078 184.817c7.483 3.737 27.438 21.919 31.928 29.267\" stroke=\"#fff\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M134.072 209.85s5.612-4.11 7.608-9.465c1.871-5.231-1.746-5.978.623-10.71 3.867-7.722-1.995-9.216.499-14.572 2.619-5.604-2.12-8.219-3.118-12.08-.997-3.861-3.118-6.476-4.864-5.978-2.993.747-2.993 4.982-5.861 7.971-2.245 2.241-4.615 6.849-3.243 10.337 1.746 4.608-4.989 6.102-1.247 13.076 4.115 7.846-5.488 9.341 9.603 21.421z\" fill=\"#3C9\"></path><path d=\"M133.948 209.85c-1.747 8.842-6.361 23.04-6.361 23.04\" stroke=\"#3C9\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M135.444 166.386c2.12 8.095 1.247 35.12-1.372 43.34\" stroke=\"#fff\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M213.892 186.188s-3.617-8.22-2.37-15.692c1.248-7.224 5.862-5.605 6.361-12.454.748-11.209 8.356-8.967 8.98-16.564.748-7.971 7.857-7.722 11.474-11.458 3.617-3.612 7.732-5.231 9.478-3.612 2.869 2.865.125 7.597 1.372 12.828.998 3.985.624 10.711-3.242 13.824-4.989 3.985 1.621 10.088-7.234 15.443-9.728 6.102 0 13.949-24.819 17.685z\" fill=\"#3C9\"></path><path d=\"M213.892 186.312c-3.991 14.198-6.361 47.948-6.361 47.948\" stroke=\"#3C9\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M240.956 136.496c-7.733 7.722-24.445 38.483-26.939 49.443\" stroke=\"#fff\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M72.586 215.828s-.125-3.861 1.621-6.476c1.746-2.615 3.243-1.246 4.49-3.861 2.12-4.234 4.74-2.117 6.236-4.981 1.621-2.989 4.365-1.744 6.36-2.616 1.996-.872 3.867-.747 4.241.125.624 1.619-1.247 2.989-1.621 5.23-.25 1.744-1.497 4.235-3.492 4.858-2.62.747-.998 4.234-5.363 4.857-4.615.871-2.12 5.479-12.472 2.864z\" fill=\"#3CA5A5\"></path><path d=\"M91.419 200.883c-4.365 1.744-15.964 11.458-18.833 15.07-3.99 4.857-8.98 18.307-8.98 18.307\" stroke=\"#3CA5A5\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M91.419 200.883c-4.24 1.744-15.964 11.084-18.708 14.945\" stroke=\"#fff\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M57.87 196.026s4.989-11.084 3.118-21.047c-1.746-9.59-7.982-7.473-8.48-16.689-.999-15.069-11.226-12.204-12.099-22.417-.997-10.835-10.476-10.461-15.465-15.318-4.988-4.857-10.476-7.099-12.72-4.733-3.867 3.861-.126 10.213-1.872 17.187-1.372 5.355-.873 14.322 4.365 18.556 6.735 5.355-2.244 13.575 9.728 20.923 13.096 7.846 0 18.557 33.425 23.538z\" fill=\"#3C9\"></path><path d=\"M57.745 196.15c3.118 14.323 3.617 38.11 3.617 38.11\" stroke=\"#3C9\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M21.452 129.148c10.352 10.337 32.926 51.809 36.293 66.629\" stroke=\"#fff\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M293.712 203.872s-3.991-8.842-2.495-16.688c1.372-7.722 6.361-5.978 6.735-13.326.748-11.956 8.855-9.714 9.603-17.809.749-8.593 8.356-8.344 12.223-12.205 3.866-3.861 8.231-5.604 10.102-3.861 3.118 3.114.125 8.095 1.496 13.7 1.123 4.234.749 11.457-3.492 14.695-5.363 4.359 1.746 10.835-7.732 16.564-10.227 6.476.124 14.945-26.44 18.93z\" fill=\"#3CA5A5\"></path><path d=\"M293.836 203.997c-2.369 11.457-2.868 30.263-2.868 30.263\" stroke=\"#3CA5A5\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M322.646 150.693c-8.231 8.22-26.19 41.223-28.81 52.93\" stroke=\"#fff\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M71.589 184.319s-3.867-8.593-2.495-16.314c1.372-7.473 6.112-5.854 6.61-12.952.749-11.583 8.606-9.341 9.354-17.312.749-8.344 8.107-8.095 11.973-11.831 3.742-3.736 8.107-5.48 9.853-3.736 2.993 2.989.125 7.846 1.497 13.326.997 4.11.748 11.084-3.368 14.322-5.238 4.234 1.746 10.461-7.483 16.066-10.227 6.351-.125 14.571-25.941 18.431z\" fill=\"#3C9\"></path><path d=\"M71.713 184.319c-7.982 23.663-2.37 49.816-2.37 49.816\" stroke=\"#3C9\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M99.775 132.635c-7.983 7.971-25.443 40.102-28.062 51.435\" stroke=\"#fff\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M119.855 204.619s3.616-8.219 2.369-15.692c-1.247-7.223-5.862-5.604-6.36-12.454-.749-11.208-8.357-8.967-8.98-16.564-.748-7.97-7.857-7.721-11.474-11.457-3.617-3.612-7.733-5.231-9.479-3.612-2.868 2.864-.124 7.597-1.372 12.828-.998 3.985-.623 10.71 3.243 13.824 4.989 3.985-1.621 10.087 7.234 15.443 9.728 6.102 0 13.948 24.819 17.684z\" fill=\"#3CA5A5\"></path><path d=\"M119.854 204.744c2.245 10.71 3.867 28.27 3.867 28.27\" stroke=\"#3CA5A5\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M92.791 154.928c7.733 7.721 24.445 38.483 26.939 49.442\" stroke=\"#fff\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M170.615 11.458c1.247-.374 2.37-1.246 2.993-2.242.375-.623.624-1.37.624-1.993 0-.747-.249-1.494-.624-2.117-.374-.872-1.122-1.37-1.87-1.744 0-.871-.375-1.619-.873-2.241a3.411 3.411 0 00-2.37-.997c-1.123-.124-2.619.374-3.492 1.246-.873-.374-1.871-.623-2.744-.374-1.247.374-2.12 1.37-2.494 2.616-.374 1.245-.125 2.615.499 3.736-.125.124-.25.249-.25.373-.499.872-.499 1.744 0 2.616.624.996 1.497 1.619 2.619 1.868\" fill=\"#EE8628\"></path><path d=\"M142.179 207.11c-1.746 3.487-1.871 9.963-1.247 14.073.374 2.491-14.592-.124-14.592-.124s6.984-11.832 7.358-19.678\" fill=\"#D39775\"></path><path d=\"M143.052 234.011h-9.104v-2.242c-2.37.125-4.116 1.121-5.862 1.868-.749.374-1.622.249-1.871.249h-18.084c-.748 0-1.372-.498-1.622-1.245-.124-.249-.124-.996-.124-.996h.748l36.044-2.491v.623l-.125 4.234z\" fill=\"#462C8C\"></path><path d=\"M128.336 218.07c-.873-.499-1.996-.125-2.62.622-4.614 5.729-14.467 7.473-17.086 9.465-1.247.872-2.12 1.993-2.37 3.238-.124.623.375 1.121.998 1.121 3.492.249 16.588.997 20.828-.373 1.123-.374 3.243-.997 4.365-1.37 2.869-.748 10.726-1.121 10.726-1.121s0-6.103-1.621-8.967c-.624-1.121-2.619.249-6.735-.125-1.497 0-4.615-1.494-6.485-2.49z\" fill=\"#2F1D5E\"></path><path d=\"M106.135 230.025c-.124.623.375 1.121.998 1.121 3.492.249 16.588.996 20.828-.373 1.123-.374 3.243-.997 4.365-1.37 2.869-.748 10.726-1.121 10.726-1.121\" stroke=\"#AC92F2\" stroke-width=\"0.406\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M122.599 221.557c.623.996 2.369 3.238 3.367 2.491.998-.872 0-2.366-1.621-3.986M118.856 223.799c.375.871 1.622 3.611 2.994 2.615 1.247-.872-.499-2.864-.873-3.736M114.741 225.542c.374 1.121 1.123 3.736 2.869 2.989 1.372-.623-.125-2.615-.624-3.861\" stroke=\"#C8B6F7\" stroke-width=\"0.406\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-dasharray=\"0.9 0.9\"></path><path d=\"M162.508 205.865c-.374 4.234 1.871 11.707-.249 15.941-1.123 2.242 14.841.374 14.841.374s-7.732-8.345-4.739-16.938\" fill=\"#D39775\"></path><path d=\"M160.388 233.264l6.61.747v-1.495c2.369.125 11.224 3.737 15.215 4.235 2.245.249 5.737.124 7.234-1.246.998-.996.374-3.362.374-3.487l-29.309-2.864v4.11h-.124z\" fill=\"#462C8C\"></path><path d=\"M176.476 218.692c2.495 2.491 4.615 5.231 6.735 7.224 2.37 2.117 4.864 3.487 5.862 4.857 1.247 1.743 1.497 3.612-1.122 4.234-5.114 1.37-9.604-.124-14.468-2.117-1.122-.498-5.737-2.615-6.984-2.864-1.871-.374-6.111-.997-6.111-.997s-.374-6.974 1.995-9.091c1.247-1.121 1.622 1.868 5.613 1.494.374 0 1.247-2.989 3.741-3.362 1.746-.125 3.617-.498 4.739.622z\" fill=\"#2F1D5E\"></path><path d=\"M188.823 229.154c1.746 3.238.749 4.359-2.494 4.982-5.363 1.12-15.59-3.861-18.708-4.982-3.492-1.245-6.735-.249-7.732-2.864\" stroke=\"#AC92F2\" stroke-width=\"0.406\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M179.345 221.557c-.624-1.245-3.367-.374-5.113.249-.499.125-.749 1.121-.624 1.495 0 .249.125.373.249.498.125.124.375.124.624.124 1.621-.124 4.116-.747 5.612-1.37M181.714 224.421c-.374-.373-.623-.498-1.247-.373-.873.124-1.621.498-2.369.747-.375.124-.624.249-.873.498-.25.249-.375.623-.125.872a.956.956 0 00.623.249c1.497.249 3.368-.747 4.864-1.245M184.333 226.663c-.873-.498-1.122.124-1.995.374-.499.124-1.247.498-1.746.871-.374.374-.624.748-.374 1.246.249.249.623.498.873.498.374 0 .748-.125.997-.125l1.871-.747c.998-.498 1.746-.623 1.746-.623\" stroke=\"#C8B6F7\" stroke-width=\"0.406\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-dasharray=\"0.9 0.9\"></path><path d=\"M160.388 49.318l5.861.747 3.867-3.238V34.995l-8.481-3.487-1.247 17.81z\" fill=\"#D39775\"></path><path d=\"M175.105 49.318c5.487.872 10.726 2.864 10.726 2.864l-1.996 45.209-38.413-.25-1.746-43.962 7.732-4.484\" fill=\"#C8B6F7\"></path><path d=\"M161.759 126.408l.873 31.509-.124 57.164 17.336.622 3.242-53.552 1.247-55.171-.498-9.714-38.414-.125-3.617 14.322-7.233 46.454-8.855 56.666 17.71.871 12.097-57.786 6.236-31.26z\" fill=\"#916DEE\"></path><path d=\"M166.499 119.434l-3.492 2.117-1.247 4.857\" stroke=\"#323232\" stroke-width=\"0.46\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path transform=\"matrix(.00821 -.0458 -.0458 -.00819 188.199 183.372)\" fill=\"url(#prefix__pattern0)\" d=\"M0 0h1345.09v1346.9H0z\"></path><path transform=\"matrix(-.02444 -.03959 -.03962 .0244 228.195 184.192)\" fill=\"url(#prefix__pattern1)\" d=\"M0 0h1345.56v1346.43H0z\"></path><path d=\"M167.621 36.49h-3.741c-5.488 0-9.978-4.483-9.978-9.963V21.42c0-6.476 5.239-11.832 11.849-11.832 6.485 0 11.848 5.231 11.848 11.832v5.106c0 5.48-4.49 9.963-9.978 9.963z\" fill=\"#D39775\"></path><path d=\"M169.867 36.49c-2.994.872-7.109.25-8.481-.124\" stroke=\"#C17E5D\" stroke-width=\"0.408\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M158.767 12.827c1.871 1.62 3.991 2.242 6.36 2.367 1.497.124 3.118 0 4.615.373 1.372.374 2.744 1.121 4.116 1.62 2.744.871 3.991-3.488 1.247-4.36-1.996-.622-3.617-1.619-5.737-1.868-2.37-.373-5.363.374-7.358-1.245-2.37-1.993-5.613 1.245-3.243 3.113z\" fill=\"#EE8628\"></path><path d=\"M176.352 13.824c-.998-1.868-2.619-3.488-4.365-4.608-2.869-1.62-7.608-1.744-10.352.124-2.494 1.62-.124 5.604 2.245 3.985 1.746-1.12 4.864-.871 6.486.499a8.006 8.006 0 011.995 2.49c1.372 2.367 5.363.125 3.991-2.49z\" fill=\"#EE8628\"></path><path d=\"M162.259 8.344c-2.993.374-5.737 2.117-7.359 4.733-.623.996-.249 2.241.624 2.989.998.747 2.12.498 2.993-.125 1.497-1.245 2.994-2.49 4.864-3.114 1.123-.373 1.996-1.494 1.622-2.74-.25-1.12-1.622-1.992-2.744-1.618-2.619.871-4.739 2.49-6.86 4.234 1.248.872 2.37 1.868 3.617 2.74.624-1.121 2.12-2.366 3.368-2.49 1.247-.125 2.244-.997 2.244-2.243-.124-1.245-1.122-2.49-2.369-2.366z\" fill=\"#EE8628\"></path><path d=\"M158.268 10.212c.623 1.869 2.12 4.484 6.734 4.608 4.365.125 5.488.499 6.985 1.246\" stroke=\"#DD741D\" stroke-width=\"0.408\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M174.356 17.311c-2.12.125-2.619-4.857-6.36-4.857-3.742 0-7.733-.747-9.728-2.242\" stroke=\"#DD741D\" stroke-width=\"0.408\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M159.515 9.216c1.372 1.37 6.36 1.743 9.852 1.37 2.619-.25 4.241 1.992 4.74 3.985.499 1.993.623 1.993 1.496 1.993\" stroke=\"#DD741D\" stroke-width=\"0.408\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M160.638 8.718c2.743.996 9.977-.25 12.347 1.245 1.871 1.121 2.494 3.114 2.494 5.355M160.638 13.948c-.873 1.37-2.494 2.117-4.241 2.99M158.891 11.956c-1.247.622-1.371 2.74-2.245 3.611M158.268 10.212c-1.622.623-2.245 2.99-2.62 4.857M159.764 13.077c-.624.996-1.372 1.494-2.494 1.494\" stroke=\"#DD741D\" stroke-width=\"0.408\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M157.02 16.813c-1.122.249-2.245.498-3.243 1.12-.873.499-1.496 1.37-1.995 2.367-.998 1.744-1.372 3.612-1.247 5.604 0 .623-.624 1.246-1.248 1.246-.748 0-1.122-.498-1.247-1.246-.249-2.366.499-4.857 1.622-6.85.623-1.12 1.496-2.241 2.619-2.988 1.122-.748 2.494-1.121 3.866-1.37.624-.125 1.247.124 1.497.871.374.25 0 1.121-.624 1.246z\" fill=\"#D39775\"></path><path d=\"M155.898 15.94c-1.123.25-2.245.499-3.243 1.122-.873.498-1.496 1.37-1.995 2.366-.998 1.743-1.372 3.611-1.247 5.604 0 .623-.624 1.246-1.248 1.246-.748 0-1.122-.499-1.247-1.246-.249-2.366.499-4.857 1.622-6.85.623-1.12 1.496-2.241 2.619-2.988 1.122-.748 2.494-1.121 3.866-1.37.623-.125 1.247.124 1.496.871.375.25 0 1.121-.623 1.246z\" fill=\"#D39775\"></path><path d=\"M148.415 27.398c-7.234 4.608-14.343 9.34-21.576 13.949-3.493 2.242-6.86-3.363-3.243-5.604 7.234-4.608 14.343-9.34 21.576-13.949 3.492-2.366 6.735 3.363 3.243 5.604z\" fill=\"#D39775\"></path><path d=\"M151.658 16.439c-.749.498-1.372.872-2.12 1.37-.375.249-.873.498-1.123.996-.623 1.121-1.372 2.242-1.995 3.363-.499.747-.873 1.37-1.372 1.992-.499.499-.998.997-1.622 1.495-.498.498-.873.872-1.122 1.619-.374.747-.873 1.494-1.123 2.366l-1.496-1.494c.748-.125 1.372.124 1.871.747.249.25.873.996.873 1.37l-.125-.125h-.624s.374-.124.499-.249c.25-.124.624-.249.873-.249.749-.249 1.372-.373 2.12-.622 1.123-.25 1.996-.25 2.994.124 1.372.498 2.743.498 4.24.25v2.24c-1.995-.373-4.116-.746-5.986-1.369a4.612 4.612 0 01-1.372-.623c-.624-.622-.624-1.619.124-1.992.499-.25 1.123-.25 1.622-.374.374 0 .623-.125.998-.125.124 0 .249 0 .374-.124.499-.125.249-.25-.125.249 0 .25-.125.374-.125.623 0 .373.499.747 0 .373-.249-.124-.499-.373-.623-.498-.375-.498-.375-.996-.25-1.619.374-1.12.624-2.242.873-3.238.25-1.12.499-2.242.624-3.363.125-.747.249-1.245.249-1.992.749.249 1.497.373 2.245.623-.623 1.12-1.871 1.992-2.744 2.864-.623.498-1.122 1.12-1.746 1.619-.374.374-.873.996-1.247 1.37.25-.125 0 0 0 0-.125-.25 0-.374 0-.498 0 .124.125.124.125.249.249.249.499.747.623.996.25.872-.249 1.744-1.122 1.993-.499.124-1.122 0-1.497.373-.249.25-.374.623-.623.872-.125.25-.374.374-.499.498-.374.374-.873.748-1.372 1.121-.499.374-1.372.125-1.621-.373-.374-.623-.125-1.246.374-1.62.499-.373.873-.747 1.247-1.12l.748-1.121c.624-.623 1.497-.747 2.37-.872h.249c.499-.124.25.125-.498.872-.25-.25-.499-.872-.624-1.121-.249-.747 0-1.37.499-1.993.873-.996 1.995-1.868 2.868-2.864.624-.498 1.123-.996 1.622-1.495l.623-.622.25-.25c-.125.25 0 0 0-.124.623-.996 2.245-.623 2.245.623 0 1.245-.375 2.49-.499 3.736-.374 1.744-.749 3.363-1.123 5.106 0 .125-.124.374-.124.498-.375.499 0-.622-.25-.498.125 0 .748.498.748.623.624.747.499 1.744-.374 2.242-.499.249-1.247.373-1.746.498-.374 0-.623.124-.998.124-.124 0-.249.125-.374.125-.374 0-.374.498.374-.498 0-.25.125-.623.125-.872 0-.125-.125-.25-.249-.374-.125-.124-.25-.124-.125-.124 0 0 .374.124.125 0 .249.124.623.249.873.249.748.249 1.496.373 2.245.623.997.249 2.12.498 3.118.622 1.122.25 1.122 2.117 0 2.242-1.622.25-3.243.374-4.864-.125-.374-.124-.873-.249-1.248-.373-.124 0-.374 0 0 0h-.249c-.249 0-.499 0-.748.124-.749.125-1.497.374-2.12.623-.25.125-.624.25-.873.25-.25.124.249-.125-.125 0-.25.124-.374.124-.624.124-.748 0-1.247-.498-1.746-.997-.125-.249-.374-.498-.499-.747l-.124-.249c0-.249-.25.125.498 0-.873.125-1.746-.498-1.496-1.494.249-.872.623-1.744 1.122-2.491.374-.748.749-1.495 1.372-2.117.499-.499 1.123-.997 1.746-1.495.624-.623 1.123-1.37 1.497-2.117.748-1.12 1.372-2.366 2.12-3.487.374-.623.998-.997 1.621-1.37l2.245-1.495c1.248-.871 2.37 1.121 1.123 1.993zM174.481 16.813c1.122.249 2.245.498 3.243 1.12.873.499 1.496 1.37 1.995 2.367.998 1.744 1.372 3.612 1.247 5.604 0 .623.624 1.246 1.247 1.246.749 0 1.123-.498 1.248-1.246.249-2.366-.499-4.857-1.622-6.85-.623-1.12-1.496-2.241-2.619-2.988-1.122-.748-2.494-1.121-3.866-1.37-.624-.125-1.247.124-1.497.871-.374.25 0 1.121.624 1.246z\" fill=\"#D39775\"></path><path d=\"M175.604 15.94c1.122.25 2.245.499 3.243 1.122.873.498 1.496 1.37 1.995 2.366.998 1.743 1.372 3.611 1.247 5.604 0 .623.624 1.246 1.247 1.246.749 0 1.123-.499 1.248-1.246.249-2.366-.499-4.857-1.622-6.85-.623-1.12-1.496-2.241-2.619-2.988-1.122-.748-2.494-1.121-3.866-1.37-.624-.125-1.247.124-1.497.871-.374.25 0 1.121.624 1.246z\" fill=\"#D39775\"></path><path d=\"M183.087 27.398c7.233 4.608 14.342 9.34 21.576 13.949 3.492 2.242 6.859-3.363 3.243-5.604-7.234-4.608-14.343-9.34-21.577-13.949-3.492-2.366-6.734 3.363-3.242 5.604z\" fill=\"#D39775\"></path><path d=\"M179.844 16.439c.748.498 1.372.872 2.12 1.37.374.249.873.498 1.122.996.624 1.121 1.372 2.242 1.996 3.363.499.747.873 1.37 1.372 1.992.499.499.997.997 1.621 1.495.499.498.873.872 1.122 1.619.375.747.874 1.494 1.123 2.366l1.497-1.494c-.749-.125-1.372.124-1.871.747-.25.25-.873.996-.873 1.37-.125.124-.125.124.124-.125h.624s-.374-.124-.499-.249c-.249-.124-.623-.249-.873-.249-.748-.249-1.372-.373-2.12-.622-1.123-.25-1.996-.25-2.993.124-1.372.498-2.744.498-4.241.25v2.24c1.996-.373 4.116-.746 5.987-1.369a4.612 4.612 0 001.372-.623c.623-.622.623-1.619-.125-1.992-.499-.25-1.122-.25-1.621-.374-.375 0-.624-.125-.998-.125-.125 0-.25 0-.374-.124-.499-.125-.25-.25.124.249 0 .25.125.374.125.623 0 .373-.499.747 0 .373.25-.124.499-.373.624-.498.374-.498.374-.996.249-1.619-.374-1.12-.623-2.242-.873-3.238-.249-1.12-.499-2.242-.623-3.363-.125-.747-.25-1.245-.25-1.992-.748.249-1.496.373-2.245.623.624 1.12 1.871 1.992 2.744 2.864.624.498 1.122 1.12 1.746 1.619.374.374.873.996 1.247 1.37-.249-.125 0 0 0 0 .125-.25 0-.374 0-.498 0 .124-.124.124-.124.249-.25.249-.499.747-.624.996-.25.872.249 1.744 1.122 1.993.499.124 1.123 0 1.497.373.249.25.374.623.624.872.124.25.374.374.498.498.375.374.874.748 1.372 1.121.499.374 1.372.125 1.622-.373.374-.623.124-1.246-.374-1.62-.499-.373-.873-.747-1.248-1.12l-.748-1.121c-.624-.623-1.497-.747-2.37-.872h-.249c-.499-.124-.249.125.499.872.249-.25.499-.872.623-1.121.25-.747 0-1.37-.498-1.993-.873-.996-1.996-1.868-2.869-2.864-.624-.498-1.122-.996-1.621-1.495l-.624-.622-.249-.25c.124.25 0 0 0-.124-.624-.996-2.245-.623-2.245.623 0 1.245.374 2.49.499 3.736.374 1.744.748 3.363 1.122 5.106 0 .125.125.374.125.498.374.499 0-.622.249-.498-.124 0-.748.498-.748.623-.624.747-.499 1.744.374 2.242.499.249 1.247.373 1.746.498.374 0 .624.124.998.124.125 0 .249.125.374.125.374 0 .374.498-.374-.498 0-.25-.125-.623-.125-.872 0-.125.125-.25.25-.374.124-.124.249-.124.124-.124 0 0-.374.124-.124 0-.25.124-.624.249-.873.249-.749.249-1.497.373-2.245.623-.998.249-2.121.498-3.118.622-1.123.25-1.123 2.117 0 2.242 1.621.25 3.242.374 4.864-.125.374-.124.873-.249 1.247-.373.125 0 .374 0 0 0h.249c.25 0 .499 0 .749.124.748.125 1.496.374 2.12.623.249.125.623.25.873.25.249.124-.25-.125.125 0 .249.124.374.124.623.124.749 0 1.247-.498 1.746-.997.125-.249.374-.498.499-.747l.125-.249c0-.249.249.125-.499 0 .873.125 1.746-.498 1.497-1.494-.25-.872-.624-1.744-1.123-2.491-.374-.748-.748-1.495-1.372-2.117-.499-.499-1.122-.997-1.746-1.495-.623-.623-1.122-1.37-1.496-2.117-.749-1.12-1.372-2.366-2.121-3.487-.374-.623-.997-.997-1.621-1.37l-2.245-1.495c-1.247-.871-2.37 1.121-1.122 1.993z\" fill=\"#D39775\"></path><path d=\"M177.225 15.567c1.122.25 2.245.498 3.243 1.121.873.498 1.496 1.37 1.995 2.366.998 1.744 1.372 3.612 1.247 5.605 0 .622.624 1.245 1.248 1.245.748 0 1.122-.498 1.247-1.245.249-2.367-.499-4.857-1.622-6.85-.623-1.12-1.496-2.242-2.619-2.989-1.122-.747-2.494-1.12-3.866-1.37-.624-.124-1.247.125-1.497.872-.374.373 0 1.12.624 1.245z\" fill=\"#D39775\"></path><path d=\"M183.211 23.662c.624 2.616 1.497 3.238 2.12 3.612M182.962 15.816c1.497 1.868 2.12 3.861 2.12 3.861M176.601 13.824c-.249.373-.249.747-.125 1.12M174.605 14.57v.374\" stroke=\"#323232\" stroke-width=\"0.46\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M154.277 15.567c-1.123.25-2.245.498-3.243 1.121-.873.498-1.496 1.37-1.995 2.366-.998 1.744-1.372 3.612-1.248 5.605 0 .622-.623 1.245-1.247 1.245-.748 0-1.122-.498-1.247-1.245-.249-2.367.499-4.857 1.621-6.85.624-1.12 1.497-2.242 2.62-2.989 1.122-.747 2.494-1.12 3.866-1.37.623-.124 1.247.125 1.496.872.375.373 0 1.12-.623 1.245z\" fill=\"#D39775\"></path><path d=\"M148.539 15.816c-1.496 1.868-2.12 3.861-2.12 3.861M154.9 13.824c.25.373.25.747.125 1.12M156.896 14.57v.374M148.539 23.289c-.623 2.615-1.496 3.238-2.12 3.611\" stroke=\"#323232\" stroke-width=\"0.46\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M175.104 15.069c-2.619 0-4.615.996-6.111 2.49-.624-.248-1.247-.373-1.871-.498-.249-.622-.748-.996-1.372-.996-.623 0-1.247.498-1.372.996l-1.87.374c-1.372-1.494-3.368-2.366-5.987-2.366-11.225 0-11.225 17.435 0 17.435 2.619 0 4.615-.871 5.987-2.366.498-.373.873-.872 1.247-1.37 1.496-2.117 1.87-4.11 1.87-6.725v-.498H166v.498c0 2.864.374 5.355 2.12 7.597 1.247 1.62 2.993 2.366 4.864 2.74.499.124.998.124 1.496.249h1.248c10.601-.747 10.351-17.56-.624-17.56z\" fill=\"#2F1D5E\"></path><path d=\"M156.396 30.637c8.855 0 8.855-13.825 0-13.825-8.855.125-8.855 13.825 0 13.825zM175.104 30.512c8.731 0 8.731-13.575 0-13.575-8.73.125-8.73 13.575 0 13.575z\" fill=\"#C8B6F7\"></path><path d=\"M181.465 23.662c.125.125-3.243 3.114-6.361 2.99-2.245-.125-4.739-1.37-6.61-2.865 0 0 6.61-7.722 12.971-.125z\" fill=\"#fff\"></path><path d=\"M175.229 26.65c.873 0 1.621-.373 2.245-.995.499-.499.998-1.495.998-2.242-.125-1.744-1.372-3.238-3.243-3.238-.873 0-1.621.373-2.245.996-.499.498-.998 1.495-.998 2.242.125 1.868 1.372 3.238 3.243 3.238z\" fill=\"#663D14\"></path><path d=\"M149.911 23.911s6.86-7.721 13.096-.249c-1.248 2.242-4.49 3.114-6.985 3.114-3.741-.125-6.111-2.865-6.111-2.865z\" fill=\"#fff\"></path><path d=\"M156.77 26.65c.873 0 1.621-.373 2.245-.995.499-.499.998-1.495.998-2.242-.125-1.744-1.372-3.238-3.243-3.238-.873 0-1.621.373-2.245.996-.499.498-.998 1.495-.998 2.242.125 1.868 1.497 3.238 3.243 3.238z\" fill=\"#663D14\"></path><path d=\"M155.648 61.647l-7.483-4.608s-8.979-2.366-23.696-5.48c-10.726-2.241-11.973-11.956-3.991-16.688 10.102-6.103 21.077-12.08 21.077-12.08l5.987 10.087-27.438 14.073 5.612-7.223s17.336 2.49 25.941 2.49h8.98l4.739 4.858 4.615-4.857h9.603c8.606 0 29.683-2.491 29.683-2.491l.624 7.223-24.944-14.073 5.987-10.087s11.474 6.226 21.077 12.08c7.982 4.857 6.735 14.447-3.991 16.688-14.467 2.99-24.944 5.48-24.944 5.48l-7.483 4.608\" fill=\"#C8B6F7\"></path><path d=\"M159.515 9.091c3.492-1.743 10.102-1.743 13.345.623\" stroke=\"#DD741D\" stroke-width=\"0.408\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M167.497 29.018l-.873.249a1.5 1.5 0 01-1.746 0l-.874-.25c-.249-.124-.249-.497-.124-.747 0 0 .623-.871.748-1.245.374-1.12.873-3.362.873-3.362a.39.39 0 01.748 0s.499 2.117.873 3.238c.125.373.624 1.245.624 1.245 0 .374 0 .747-.249.872z\" fill=\"#C17E5D\"></path><path d=\"M165.751 30.637c-1.247 0-1.497 1.494 0 1.494 1.496.125 1.496-1.37 0-1.494z\" fill=\"#323232\"></path><path d=\"M208.155 211.345s2.993-6.725 1.87-12.579c-.997-5.729-4.739-4.483-5.113-9.963-.624-8.967-6.735-7.223-7.234-13.326-.623-6.476-6.236-6.227-9.229-9.216-2.993-2.989-6.236-4.234-7.608-2.864-2.369 2.366 0 6.102-1.122 10.337-.748 3.238-.499 8.593 2.619 11.084 3.991 3.238-1.372 8.095 5.862 12.454 7.732 4.732-.125 11.084 19.955 14.073z\" fill=\"#3CA5A5\"></path><path d=\"M208.154 211.469c1.871 8.594 3.118 22.791 3.118 22.791\" stroke=\"#3CA5A5\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M186.329 171.368c6.236 6.227 19.706 31.01 21.701 39.853\" stroke=\"#fff\" stroke-width=\"0.5\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M185.705 53.552v2.99M199.176 40.85l6.236 3.486M125.716 44.087l6.859-3.487M143.925 53.552v2.491\" stroke=\"#323232\" stroke-width=\"0.46\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path transform=\"matrix(-.00824 -.03919 -.0392 .00821 254.08 67.584)\" fill=\"url(#prefix__pattern2)\" d=\"M0 0h1345.12v1346.88H0z\"></path><path transform=\"matrix(-.0028 .03995 .03995 .0028 102.595 29.738)\" fill=\"url(#prefix__pattern3)\" d=\"M0 0h1345.04v1346.95H0z\"></path></g><defs><pattern id=\"prefix__pattern0\" patternContentUnits=\"objectBoundingBox\" width=\"1\" height=\"1\"><use xlink:href=\"#prefix__image0\"></use></pattern><pattern id=\"prefix__pattern1\" patternContentUnits=\"objectBoundingBox\" width=\"1\" height=\"1\"><use xlink:href=\"#prefix__image0\"></use></pattern><pattern id=\"prefix__pattern2\" patternContentUnits=\"objectBoundingBox\" width=\"1\" height=\"1\"><use xlink:href=\"#prefix__image0\"></use></pattern><pattern id=\"prefix__pattern3\" patternContentUnits=\"objectBoundingBox\" width=\"1\" height=\"1\"><use xlink:href=\"#prefix__image0\"></use></pattern><clipPath id=\"prefix__clip0\"><path fill=\"#fff\" d=\"M0 0h332v237H0z\"></path></clipPath><image id=\"prefix__image0\" xlink:href=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABDgAAAQ4CAYAAADsEGyPAAAACXBIWXMAAAsSAAALEgHS3X78AAAA\"></image></defs></svg>";
	
	//*******other page locators starts here*******//
	By libraryTabHeading		= By.xpath(".//*[@class='rdznaLink' and text()='Library'] | .//*[@href='/library']");
	String libraryHeading		= ".//h2[text()='$$Library$$']";
	By toastMessage 			= By.cssSelector(".toast-message, #client-snackbar");
	By callsTab					= By.cssSelector("[data-testid='conversations-tab']");
	By savedSearchTab			= By.cssSelector("[data-testid='saved search-tab']");
	//*******other page locators ends here*******//
	
	//*******Create and Delete Library locators starts here*******//
	By libraryHelperText		= By.cssSelector("#library-helper-text");
	public By createNewLibraryBtn		= By.xpath(".//button[text()='Create New Library']");
	By newLibraryTextBox		= By.cssSelector("input[name='library']");
	By saveLibraryButton		= By.xpath(".//button[text()='Save']");
	By cancelLibraryButton		= By.xpath(".//button[text()='Cancel']");
	By editLibraryButton		= By.xpath(".//*[text()='trash-can']/../../preceding-sibling::div/*");
	By deleteLibraryButton		= By.xpath(".//*[text()='trash-can']/..");
	By confirmDeleteButton		= By.xpath(".//button[text()='Confirm']");
	//*******Create and Delete Library locators ends here*******//
	
	//*******Left Nav Library List locators starts here*******//
	By librayList				= By.cssSelector("div[name*='library']");
	//*******Left Nav Library List locators ends here*******//
	
	//*******Left Nav Library List locators starts here*******//
	By noResultImage			= By.cssSelector("div.no-search-results svg");
	By noResultTextHeading		= By.cssSelector("div.no-search-results h2");
	By noResultTextPara			= By.cssSelector("div.no-search-results span");
	//*******Left Nav Library List locators ends here*******//
	
	/**
	 * use this method to verify if user is on library tab
	 * it verify is library tab is blue colored and having blue bottom border
	 * also verifies that create new library button is there and library as heading
	 * @param driver
	 * @param library that is appearing in heading
	 */
	public void verifyUserOnLibraryPage(WebDriver driver, String library) {
		assertEquals(getCssValue(driver, findElement(driver, libraryTabHeading), CssValues.BorderColor), "rgb(0, 0, 238)");
		assertTrue(isElementVisible(driver, createNewLibraryBtn, 5));
		assertTrue(isElementVisible(driver, By.xpath(libraryHeading.replace("$$Library$$", library)), 10));
	}
	
	/**
	 * find if library tab is visible or not
	 * @param driver
	 */
	public boolean isLibraryTabVisible(WebDriver driver) {
		return isElementVisible(driver, libraryTabHeading, 5);
	}
	
	/**
	 * use this method to navigate to library page
	 * @param driver
	 */
	public void openLibraryPage(WebDriver driver) {
		clickElement(driver, libraryTabHeading);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * use this method to navigate to calls tab of library page
	 * @param driver
	 */
	public void clickCallsTab(WebDriver driver) {
		clickElement(driver, callsTab);
	}
	
	/**
	 * use this method to navigate to Saved Search tab of library page
	 * @param driver
	 */
	public void clickSavedSearchTab(WebDriver driver) {
		clickElement(driver, savedSearchTab);
	}
	
	
	/**
	 * @param driver
	 */
	public void verifyErrorEmptyLibrary(WebDriver driver) {
		clickElement(driver, createNewLibraryBtn);
		waitUntilVisible(driver, newLibraryTextBox);
		clickElement(driver, newLibraryTextBox);
		clickElement(driver, saveLibraryButton);
		waitUntilVisible(driver, libraryHelperText);
		assertEquals(getElementsText(driver, libraryHelperText), "Please enter a library name.");
		assertTrue(findElement(driver, newLibraryTextBox).findElement(By.xpath("..")).getAttribute("class").contains("Mui-error"));
		clickElement(driver, cancelLibraryButton);
	}
	
	/**
	 * Use this method to select library using library name
	 * @param driver
	 * @param library name of the library to be selected
	 */
	public void selectLibrary(WebDriver driver, String library) {
		int index = getTextListFromElements(driver, librayList).indexOf(library);
		if(index >= 0) {
			getLibraryList(driver).get(index).click();
		}
		callsTabReactPage.waitTillProgressCircleInvisible(driver);
	}
	
	/**
	 * Use this method to select library by index
	 * @param driver
	 * @param index index of the library to be selected
	 * @return name of the library which is selected
	 */
	public String selectLibraryByIndex(WebDriver driver, int index) {
		if(index >= 0) {
			getLibraryList(driver).get(index).click();
		}
		callsTabReactPage.waitTillProgressCircleInvisible(driver);
		return getTextListFromElements(driver, librayList).get(index);
	}
	
	/**
	 * use this method to update the Library name
	 * @param driver
	 * @param libraryName name to which library name has to be changed
	 */
	public void editLibrary(WebDriver driver, String libraryName) {
		clickElement(driver, editLibraryButton);
		clearAll(driver, newLibraryTextBox);
		addLibraryDetails(driver, libraryName);
		waitUntilInvisible(driver, toastMessage);
		assertTrue(isLibraryExistInList(driver, libraryName));
		assertTrue(isElementVisible(driver, By.xpath(libraryHeading.replace("$$Library$$", libraryName)), 10));
	}
	
	/**
	 * use this method to create the library
	 * @param driver
	 * @param libraryName name with which library needs to be created
	 */
	public void createLibrary(WebDriver driver, String libraryName) {
		clickElement(driver, createNewLibraryBtn);
		addLibraryDetails(driver, libraryName);
		assertEquals(getElementsText(driver, toastMessage), "Library "+ libraryName +" created.");
		waitUntilInvisible(driver, toastMessage);
		assertTrue(isLibraryExistInList(driver, libraryName));
		assertTrue(isElementVisible(driver, By.xpath(libraryHeading.replace("$$Library$$", libraryName)), 10));
	}
	
	
	/**
	 * use this method to enter the details of library in new/edit library window
	 * @param driver
	 * @param libraryName
	 */
	private void addLibraryDetails(WebDriver driver, String libraryName) {
		waitUntilVisible(driver, newLibraryTextBox);
		enterText(driver, newLibraryTextBox, libraryName);
		clickElement(driver, saveLibraryButton);
		waitUntilInvisible(driver, saveLibraryButton);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, toastMessage);
	}
	
	/**
	 * use this method to verify if library exists in the list or not
	 * @param driver
	 * @param libraryName name of the library which needs to be verified if exists or not
	 * @return boolean value true if library present else false
	 */
	public Boolean isLibraryExistInList(WebDriver driver, String libraryName) {
		List<String> libraryList = getTextListFromElements(driver, librayList);
		return libraryList!=null ? libraryList.contains(libraryName): false;
	}
	
	/**
	 * 
	 * @param driver
	 * @param libraryName name of the library which needs to be verified if exists or not
	 * @return boolean value true if library present else false
	 */
	public boolean isLibrarySelectedAndHighlighted(WebDriver driver, String libraryName) {
		isListElementsVisible(driver, librayList, 5);
		int index = getTextListFromElements(driver, librayList).indexOf(libraryName);
		return getLibraryList(driver).get(index).getAttribute("style").equals("background-color: rgb(234, 234, 234);")
				&& isElementVisible(driver, By.xpath(libraryHeading.replace("$$Library$$", libraryName)), 5);
	}
	
	/**
	 * @param driver
	 * @param libraryName
	 */
	public void verifyFirstLibrarySelectedDefaultHighlighted(WebDriver driver, String libraryName) {
		isListElementsVisible(driver, librayList, 5);
		assertTrue(getLibraryList(driver).get(0).getAttribute("style").equals("background-color: rgb(234, 234, 234);"));
		assertTrue(isElementVisible(driver, By.xpath(libraryHeading.replace("$$Library$$", libraryName)), 5));
	}
	
	/**
	 * use this method to get the lists of the Library
	 * @param driver
	 * @return list of the libraries
	 */
	public List<WebElement> getLibraryList(WebDriver driver) {
		return getElements(driver, librayList);
	}
	
	/**
	 * use this method to delete library
	 * @param driver
	 * @param libraryName name of the library which needs to be deleted
	 */
	public void deletLibrary(WebDriver driver, String libraryName) {
		int index = getTextListFromElements(driver, librayList).indexOf(libraryName);
		if(index >= 0) {
			getLibraryList(driver).get(index).click();
			dashboard.isPaceBarInvisible(driver);
			waitUntilVisible(driver, deleteLibraryButton);
			clickElement(driver, deleteLibraryButton);
			clickElement(driver, confirmDeleteButton);
			waitUntilInvisible(driver, confirmDeleteButton);
			waitUntilVisible(driver, toastMessage);
			assertEquals(getElementsText(driver, toastMessage), "Library "+ libraryName +" deleted.");
			waitUntilInvisible(driver, toastMessage);
			dashboard.isPaceBarInvisible(driver);
			System.out.println("successfully deleted library " + libraryName);
		}else {
			System.out.println("No such library with name " + libraryName + " to delete");
		}
	}
	
	/**
	 * use this method to verify that agent with no CAI manager setting
	 * has no access to the create, edit and delete library button
	 * @param driver
	 */
	public void noAccessForAgentCamOff(WebDriver driver) {
		waitUntilInvisible(driver, createNewLibraryBtn);
		waitUntilInvisible(driver, editLibraryButton);
		waitUntilInvisible(driver, deleteLibraryButton);
	}
	
	/**
	 * use this method to verify that there are no call associated with library on Call tab
	 * Verifies image and message
	 * @param driver
	 */
	public void verifyNoLibraryCallMessage(WebDriver driver) {
		waitUntilVisible(driver, noResultImage);
		assertEquals(getAttribue(driver, noResultImage, ElementAttributes.outerHTML), noResultImgOuterHtml);
		assertEquals(getElementsText(driver, noResultTextHeading), "You currently have no calls in this library");
		assertEquals(getElementsText(driver, noResultTextPara), "Add calls to this library using the action button on the calls page!");
	}
	
	/**
	 * use this method to verify that there are no search results on Call tab
	 * Verifies image and message
	 * @param driver
	 */
	public void verifyLibraryNoSearchMessage(WebDriver driver) {
		waitUntilVisible(driver, noResultImage);
		assertEquals(getAttribue(driver, noResultImage, ElementAttributes.outerHTML), noSearchResultOuterHtml);
		assertEquals(getElementsText(driver, noResultTextHeading), "Nothing to see here");
		assertEquals(getElementsText(driver, noResultTextPara), "Your search found no results.");
	}
}
