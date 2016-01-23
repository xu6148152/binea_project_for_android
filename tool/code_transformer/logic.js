function bind(inputEl, outputEl, convert) {
  if (typeof inputEl == 'string') {
    inputEl = document.getElementById(inputEl);
  }
  if (typeof outputEl == 'string') {
    outputEl = document.getElementById(outputEl);
  }

  var value = inputEl.value;
  outputEl.value = convert(value);
  inputEl.addEventListener('input', function() {
    var value = inputEl.value;
    outputEl.value = convert(value);
  });
}

function convertSketchCSS(input) {
  var output = input.replace(/;/g, ',')
  // replace comments
  .replace(/^\/\*.*\*\/\n/m, '')
  // convert font-family to fontFamily
  .replace(/(\w+)-(\w)(\w+:)/g, function(a) {
    var parts = a.split('-');
    return parts[0] + parts[1][0].toUpperCase() + parts[1].substring(1);
  })
  // change 32px to 16
  .replace(/: \d+px/g, function (a) {
    return ': ' + parseInt(a.substring(2), 10) / 2.0;
  })
  // convert color
  .replace(/: #(.*),/g, ": '#$1',")
  // convert fontFamily name to string
  .replace(/fontFamily: (.*),/g, "fontFamily: '$1',")

  output = "{\n" + output.replace(/^/mg, '  ') + "\n}"
  return output;
}

var tagsMap = {
  v: 'View',
  t: 'Text',
  i: 'Image',
  to: 'TouchableOpacity',
  sv: 'ScrollView',
};

// return start_tag, end_tag
function expandLineToJSX(line) {
  line = line.trim()
  var parts = line.split(/\s+/)
  tag = tagsMap[parts[0]]
  if (!tag) {
    tag = parts[0];
  }

  var props = {}
  var style = null;
  if (parts[1] && parts[1].substring(0, 2) == 's.') {
    style = parts[1].substring(2)
    props.style = '{styles.' + style + '}'
  }
  var content = parts.slice(2).join(' ')
  if (tag == 'Image') {
    props.source = '{Images.()}'
  }

  if (tag.indexOf('Touch') == 0) {
    props.onPress = '{() => {}}'
    props.activeOpacity = '{0.8}'
  }
  var props_map = [];
  for (k in props) {
    props_map.push(k + '=' + props[k]);
  }
  var props_string = props_map.join(" ")
  if (props_string) props_string = ' ' + props_string
  var start_tag = '<' + tag + props_string + '>'
  if (tag == 'Text') {
    start_tag = start_tag + "\n  " + content
  }
  var end_tag = '</' + tag + '>';
  return [start_tag, end_tag, style];
}

function prependWithSpaces(str, spaces) {
  return Math.pow(10, spaces).toString().replace('1', '').replace(/0/g, ' ') + str
}

function convertToJSX(input) {
  var lines = input.split("\n");
  var last_indent = -1;
  // var start_tag = [];
  var end_tags = [];
  var results = [];
  var styles = [];

  lines.forEach( function (line) {
    line = line.replace(/\s*$/m, '')
    var stripped = line.trim();
    if (!stripped) return;
    var spaces = line.length - stripped.length;

    var tags = expandLineToJSX(stripped);
    var start_tag = prependWithSpaces(tags[0], spaces);
    var end_tag = prependWithSpaces(tags[1], spaces);
    var style = tags[2];
    if (style && styles.indexOf(style) == -1) styles.push(style);

    if (last_indent == -1) {
      results.push(start_tag);
      end_tags.push(end_tag)
      last_indent = spaces;
      return;
    }

    if (spaces <= last_indent) {
      var diff_indent = Math.ceil((last_indent - spaces) / 2)
      for (var i = 0, len = diff_indent + 1; i < len; i++) {
        var last_end_tag = end_tags.pop();
        if (last_end_tag) {
          results.push(last_end_tag)
        }
      }
    }

    results.push(start_tag)
    last_indent = spaces
    end_tags.push(end_tag)
  });

  var end_tag;
  while (end_tag = end_tags.pop()) {
    results.push(end_tag)
  }

  var styles_output = styles.map( function (a) {
    return '  ' + a + ": {\n    \n  },";
  });

  var output = results.join("\n") + "\n\n\nstyles:\n\n" + styles_output.join("\n") + "\n\n";
  return output;
}

bind('sketch-css', 'rn-style', convertSketchCSS);
bind('view-code', 'rn-jsx', convertToJSX);

// setTimeout(function() { location.reload(); }, 10000);
